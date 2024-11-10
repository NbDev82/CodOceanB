package com.example.codoceanb.report.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.report.dto.ReportDTO;
import com.example.codoceanb.report.dto.ViolationTypeDTO;
import com.example.codoceanb.report.entity.Report;
import com.example.codoceanb.report.entity.ViolationType;
import com.example.codoceanb.report.respository.ReportRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;

    @Override
    public boolean createReport(ReportDTO reportDTO, String authHeader) {
        User user = userService.getUserDetailsFromToken(authHeader);

        List<ViolationType> violationTypes = reportDTO.getViolationTypes().stream().map(ViolationTypeDTO::toEntity).toList();

        Report report = Report.builder()
                .type(reportDTO.getType())
                .description(reportDTO.getDescription())
                .violationTypes(violationTypes)
                .owner(user)
                .violationId(reportDTO.getViolationId())
                .build();
        reportRepository.save(report);
        return true;
    }
}
