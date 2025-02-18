package com.example.codoceanb.admin.report.controller;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.admin.report.service.problem.ProblemReportService;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/v1/reports/problem")
public class ProblemReportController {

    @Autowired
    private ProblemReportService problemReportService;

    @GetMapping("/list")
    public ResponseEntity<List<ProblemDTO>> getReportedProblems() {
        List<ProblemDTO> reportedDiscusses = problemReportService.getReportedProblems();
        return ResponseEntity.ok(reportedDiscusses);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<ReportDTO>> getProblemReports(@PathVariable("id") UUID problemId) {
        List<ReportDTO> reports = problemReportService.getProblemReports(problemId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable("id") UUID id) {
        ReportDTO report = problemReportService.getReportById(id);
        return ResponseEntity.ok().body(report);
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<Void> lockReport(@PathVariable("id") UUID id, @RequestBody String reason) {
        problemReportService.lockReport(id, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/warn/{id}")
    public ResponseEntity<Void> warnReport(@PathVariable("id") UUID id, @RequestBody String reason) {
        problemReportService.warnReport(id, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ignore/{id}")
    public ResponseEntity<Void> ignoreReport(@PathVariable("id") UUID id) {
        problemReportService.ignoreReport(id);
        return ResponseEntity.ok().build();
    }
}
