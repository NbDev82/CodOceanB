package com.example.codoceanb.admin.report.service.problem;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.admin.report.exception.ResourceNotFoundException;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import com.example.codoceanb.report.entity.Report;
import com.example.codoceanb.report.respository.ReportRepository;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.problem.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProblemReportServiceImpl implements ProblemReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public List<ProblemDTO> getReportedProblems() {
        List<Report> reports = reportRepository.findByTypeAndStatus(Report.EReportType.PROBLEM, Report.EStatus.WAITING);
        Set<UUID> violationIds = reports.stream().map(Report::getViolationId).collect(Collectors.toSet());
        List<ProblemDTO> problemDTOs = new ArrayList<>();

        for(UUID id : violationIds) {
            problemRepository.findById(id).ifPresent(problem -> problemDTOs.add(problem.toDTO()));
        }
        return problemDTOs;
    }

    @Override
    public List<ReportDTO> getProblemReports(UUID problemId) {
        List<Report> reports = reportRepository.findByStatusAndTypeAndViolationId(Report.EStatus.WAITING, Report.EReportType.PROBLEM, problemId);
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
                Problem problem = problemRepository.findById(report.getViolationId()).orElseThrow(ResourceNotFoundException::new);
                problem.setDeleted(true);
                problemRepository.save(problem);

                report.setStatus(Report.EStatus.FINISHED);
                report.setReason(reason);
                reportRepository.save(report);
                List<Report> reports = reportRepository.findByViolationId(report.getViolationId()).stream()
                        .filter(r -> !r.getId().equals(report.getId()))
                        .toList();

                reportRepository.deleteAll(reports);

                // Gửi email về việc khóa bài toán
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
