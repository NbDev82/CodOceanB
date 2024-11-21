package com.example.codoceanb.admin.report.service.comment;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.admin.report.exception.ResourceNotFoundException;
import com.example.codoceanb.comment.dto.CommentDTO;
import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.comment.repository.CommentRepository;
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
public class CommentReportServiceImpl implements CommentReportService {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getReportedComments() {
        List<Report> reports = reportRepository.findByTypeAndStatus(Report.EReportType.COMMENT, Report.EStatus.WAITING);
        Set<UUID> violationIds = reports.stream().map(Report::getViolationId).collect(Collectors.toSet());
        List<CommentDTO> commentDTOs = new ArrayList<>();

        for(UUID id : violationIds) {
            commentRepository.findById(id).ifPresent(comment -> commentDTOs.add(comment.toDTO()));
        }
        return commentDTOs;
    }

    @Override
    public List<ReportDTO> getReports(UUID commentId) {
        List<Report> reports = reportRepository.findByStatusAndTypeAndViolationId(Report.EStatus.WAITING, Report.EReportType.COMMENT, commentId);
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
                Comment comment = commentRepository.findById(report.getViolationId()).orElseThrow(ResourceNotFoundException::new);
                comment.setDeleted(true);
                commentRepository.save(comment);

                report.setStatus(Report.EStatus.FINISHED);
                report.setReason(reason);
                reportRepository.save(report);
                reportRepository.deleteAll(reportRepository.findByViolationId(report.getViolationId()).stream()
                        .filter(r -> !r.getId().equals(report.getId()))
                        .collect(Collectors.toList()));

                // Gửi email về việc khóa bình luận
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
