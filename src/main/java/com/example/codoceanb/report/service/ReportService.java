package com.example.codoceanb.report.service;

import com.example.codoceanb.report.dto.ReportDTO;
import com.example.codoceanb.report.dto.ViolationTypeDTO;
import com.example.codoceanb.report.entity.Report;

import java.util.List;

public interface ReportService {
    boolean createReport(ReportDTO reportDTO, String authHeader);

    List<ViolationTypeDTO> getViolations(Report.EReportType type);
}
