package com.example.codoceanb.admin.report.controller;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.admin.report.service.discuss.DiscussReportService;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/v1/reports/discuss")
public class DiscussReportController {

    @Autowired
    private DiscussReportService discussReportService;

    @GetMapping("/list")
    public ResponseEntity<List<DiscussDTO>> getReportedDiscusses() {
        List<DiscussDTO> reportedDiscusses = discussReportService.getReportedDiscusses();
        return ResponseEntity.ok(reportedDiscusses);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<ReportDTO>> getDiscussReports(@PathVariable(name = "id") UUID discussId) {
        List<ReportDTO> reports = discussReportService.getDiscussReports(discussId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable("id") UUID id) {
        ReportDTO report = discussReportService.getReportById(id);
        return ResponseEntity.ok().body(report);
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<Void> lockReport(@PathVariable("id") UUID id, @RequestBody String reason) {
        discussReportService.lockReport(id, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/warn/{id}")
    public ResponseEntity<Void> warnReport(@PathVariable("id") UUID id, @RequestBody String reason) {
        discussReportService.warnReport(id, reason);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ignore/{id}")
    public ResponseEntity<Void> ignoreReport(@PathVariable("id") UUID id) {
        discussReportService.ignoreReport(id);
        return ResponseEntity.ok().build();
    }
}