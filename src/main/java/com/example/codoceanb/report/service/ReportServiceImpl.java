package com.example.codoceanb.report.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.report.dto.ReportDTO;
import com.example.codoceanb.report.dto.ViolationTypeDTO;
import com.example.codoceanb.report.entity.Report;
import com.example.codoceanb.report.entity.ViolationType;
import com.example.codoceanb.report.respository.ReportRepository;
import com.example.codoceanb.report.respository.ViolationTypeRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService{

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ViolationTypeRepository violationTypeRepository;

    @Autowired
    private UserService userService;

    @Override
    public boolean createReport(ReportDTO reportDTO, String authHeader) {
        User user = userService.getUserDetailsFromToken(authHeader);

        Report report = Report.builder()
                .type(reportDTO.getType())
                .description(reportDTO.getDescription())
                .status(Report.EStatus.WAITING)
                .createdAt(LocalDateTime.now())
                .violationId(reportDTO.getViolationId())
                .owner(user)
                .build();
        Report savedReport = reportRepository.save(report);

        List<ViolationType> violationTypes = reportDTO.getViolationTypes().stream().map(v -> v.toEntity(savedReport)).toList();
        violationTypeRepository.saveAll(violationTypes);

        return true;
    }

    @Override
    public List<ViolationTypeDTO> getViolations(Report.EReportType type) {
        List<ViolationTypeDTO> violationTypeDTOs = new ArrayList<>();
        switch (type) {
            case PROBLEM:
                violationTypeDTOs.addAll(Arrays.stream(ViolationType.ProblemViolationType.values()).map(e -> ViolationTypeDTO.builder().description(e.getDescription()).build()).toList());
                break;
            case DISCUSS:
                violationTypeDTOs.addAll(Arrays.stream(ViolationType.DiscussViolationType.values()).map(e -> ViolationTypeDTO.builder().description(e.getDescription()).build()).toList());
                break;
            case COMMENT:
                violationTypeDTOs.addAll(Arrays.stream(ViolationType.CommentViolationType.values()).map(e -> ViolationTypeDTO.builder().description(e.getDescription()).build()).toList());
                break;
            default:
                return null;
        }
        return violationTypeDTOs;
    }
}
