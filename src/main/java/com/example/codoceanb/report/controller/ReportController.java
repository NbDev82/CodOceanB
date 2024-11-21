package com.example.codoceanb.report.controller;

import com.example.codoceanb.report.dto.ReportDTO;
import com.example.codoceanb.report.dto.ViolationTypeDTO;
import com.example.codoceanb.report.entity.Report;
import com.example.codoceanb.report.entity.ViolationType;
import com.example.codoceanb.report.service.ReportService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/violations")
    public ResponseEntity<List<ViolationTypeDTO>> getViolations(@RequestParam Report.EReportType type) {
        return ResponseEntity.ok(reportService.getViolations(type));
    }

    @PostMapping
    public ResponseEntity<Boolean> createReport(@RequestBody @NonNull ReportDTO report,
                                                @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(reportService.createReport(report, authHeader));
    }
}
