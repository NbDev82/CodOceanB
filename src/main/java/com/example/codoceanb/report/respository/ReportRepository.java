package com.example.codoceanb.report.respository;

import com.example.codoceanb.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByTypeAndStatus(Report.EReportType eReportType, Report.EStatus eStatus);
    List<Report> findByStatusAndTypeAndViolationId(Report.EStatus eStatus, Report.EReportType eReportType, UUID commentId);

    void deleteByViolationId(UUID violationId);

    List<Report> findByViolationId(UUID violationId);
}
