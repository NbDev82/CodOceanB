package com.example.codoceanb.submitcode.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemDTO {
    private UUID id;
    private String title;
    private String description;
    private String difficulty;
    private String outputDataType;
    private ProblemHintDTO problemHintDTO;
    private int acceptedCount;
    private int discussCount;
    private int submissionCount;
    private String acceptanceRate;
}
