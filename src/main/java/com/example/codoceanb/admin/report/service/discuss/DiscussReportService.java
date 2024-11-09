package com.example.codoceanb.admin.report.service.discuss;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.discuss.dto.DiscussDTO;

import java.util.List;
import java.util.UUID;


public interface DiscussReportService {
    List<DiscussDTO> getReportedDiscusses();

    List<ReportDTO> getDiscussReports(UUID discussId);

    ReportDTO getReportById(UUID id);

    void lockReport(UUID id, String reason);

    void warnReport(UUID id, String reason);

    void ignoreReport(UUID id);
}
