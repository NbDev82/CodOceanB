package com.example.codoceanb.admin.report.service.problem;

import com.example.codoceanb.admin.report.dto.ReportDTO;
import com.example.codoceanb.submitcode.DTO.ProblemDTO;

import java.util.List;
import java.util.UUID;

public interface ProblemReportService {
    List<ProblemDTO> getReportedProblems();

    List<ReportDTO> getProblemReports(UUID problemId);

    ReportDTO getReportById(UUID id);

    void lockReport(UUID id, String reason);

    void warnReport(UUID id, String reason);

    void ignoreReport(UUID id);
}
