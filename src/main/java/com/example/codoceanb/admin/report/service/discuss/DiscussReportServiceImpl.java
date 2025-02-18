package com.example.codoceanb.admin.report.service.discuss;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.admin.report.exception.ResourceNotFoundException;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.discuss.entity.Discuss;
import com.example.codoceanb.discuss.repository.DiscussRepository;
import com.example.codoceanb.report.entity.Report;
import com.example.codoceanb.report.respository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DiscussReportServiceImpl implements DiscussReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private DiscussRepository discussRepository;

    @Override
    public List<DiscussDTO> getReportedDiscusses() {
        List<Report> reports = reportRepository.findByTypeAndStatus(Report.EReportType.DISCUSS, Report.EStatus.WAITING);
        Set<UUID> violationIds = reports.stream().map(Report::getViolationId).collect(Collectors.toSet());
        List<DiscussDTO> discussDTOs = new ArrayList<>();

        for(UUID id : violationIds) {
            discussRepository.findById(id).ifPresent(discuss -> discussDTOs.add(discuss.toDTO(null)));
        }
        return discussDTOs;
    }

    @Override
    public List<ReportDTO> getDiscussReports(UUID discussId) {
        List<Report> reports = reportRepository.findByStatusAndTypeAndViolationId(Report.EStatus.WAITING, Report.EReportType.DISCUSS, discussId);
        return reports.stream().map(Report::toDTO).collect(Collectors.toList());
    }

    @Override
    public ReportDTO getReportById(UUID id) {
        Report report = reportRepository.findById(id).orElse(null);
        return report != null ? report.toDTO() : null;
    }

    @Override
    public void lockReport(UUID id, String reason) {
        synchronized (this) {
            Report report = reportRepository.findById(id).orElseThrow();
            if(report.getStatus().equals(Report.EStatus.WAITING)) {
                Discuss discuss = discussRepository.findById(report.getViolationId()).orElseThrow(ResourceNotFoundException::new);
                discuss.setClosed(true);
                discussRepository.save(discuss);

                report.setStatus(Report.EStatus.FINISHED);
                report.setReason(reason);
                reportRepository.save(report);
                reportRepository.deleteAll(reportRepository.findByViolationId(report.getViolationId()).stream()
                        .filter(r -> !r.getId().equals(report.getId()))
                        .collect(Collectors.toList()));

                // Gửi email về việc khóa bài đăng
            }
        }
    }

    @Override
    public void warnReport(UUID id, String reason) {
        synchronized (this) {
            Report report = reportRepository.findById(id).orElseThrow();
            if(report.getStatus().equals(Report.EStatus.WAITING)) {
                report.setStatus(Report.EStatus.FINISHED);
                report.setReason(reason);
                reportRepository.save(report);
    
                // Gửi email về việc cảnh báo
            }
        }
    }

    @Override
    public void ignoreReport(UUID id) {
        synchronized (this) {
            Report report = reportRepository.findById(id).orElseThrow();
            if(report.getStatus().equals(Report.EStatus.WAITING)) {
                report.setStatus(Report.EStatus.FINISHED);
                reportRepository.save(report);
            }
        }
    }
}
