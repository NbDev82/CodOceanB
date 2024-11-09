package com.example.codoceanb.report.service;

import com.example.codoceanb.report.dto.ReportDTO;

public interface ReportService {
    boolean createReport(ReportDTO reportDTO, String authHeader);
}
