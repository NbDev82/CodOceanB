package com.example.codoceanb.admin.report.dto;

import com.example.codoceanb.report.entity.Report;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportDTO {
    private UUID id;
    private Report.EReportType type;
    private String description;
    private UUID violationId;
    private Report.EStatus status;
    private LocalDateTime createdAt;
    private UUID ownerId;
    private String ownerName;
    private List<ViolationTypeDTO> violationTypes;

    private boolean isClosed;
    private EResult result;

    @Getter
    public enum EReportType {
        PROBLEM,
        DISCUSS,
        COMMENT
    }

    @Getter
    public enum EResult {
        ALERT,
        IGNORE,
        BAN
    }
}
