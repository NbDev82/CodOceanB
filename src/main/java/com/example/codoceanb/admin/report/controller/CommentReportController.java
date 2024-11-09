package com.example.codoceanb.admin.report.controller;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.admin.report.service.comment.CommentReportService;
import com.example.codoceanb.auth.dto.UserDTO;
import com.example.codoceanb.comment.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/v1/reports/comment")
public class CommentReportController {

    @Autowired
    private CommentReportService commentReportService;

    @GetMapping("/list")
    public ResponseEntity<List<CommentDTO>> getReportedComments() {
        List<CommentDTO> reportedDiscusses = commentReportService.getReportedComments();
        return ResponseEntity.ok(reportedDiscusses);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<ReportDTO>> getCommentReports(@PathVariable("id") UUID commentId) {
        List<ReportDTO> reports = commentReportService.getReports(commentId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable("id") UUID id) {
        ReportDTO report = commentReportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<Void> lockReport(@PathVariable("id") UUID id, @RequestBody String reason) {
        commentReportService.lockReport(id, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/warn/{id}")
    public ResponseEntity<Void> warnReport(@PathVariable("id") UUID id, @RequestBody String reason) {
        commentReportService.warnReport(id, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ignore/{id}")
    public ResponseEntity<Void> ignoreReport(@PathVariable("id") UUID id) {
        commentReportService.ignoreReport(id);
        return ResponseEntity.ok().build();
    }
}
