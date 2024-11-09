package com.example.codoceanb.report.dto;

import com.example.codoceanb.report.entity.ViolationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ViolationTypeDTO {
    private UUID id;
    private String description;

    public ViolationType toEntity() {
        return ViolationType.builder()
                .description(description)
                .build();
    }
}
