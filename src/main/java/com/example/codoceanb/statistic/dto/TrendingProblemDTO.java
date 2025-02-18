package com.example.codoceanb.statistic.dto;

import com.example.codoceanb.submitcode.problem.entity.Problem;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrendingProblemDTO {
    private UUID id;
    private String title;
    private String description;
    private String difficulty;
    private int acceptedCount;
    private int discussCount;
    private int submissionCount;
    private String acceptanceRate;
    private List<Problem.ETopic> topics;
}
