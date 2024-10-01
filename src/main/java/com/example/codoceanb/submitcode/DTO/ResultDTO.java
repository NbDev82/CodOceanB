package com.example.codoceanb.submitcode.DTO;

import com.example.codoceanb.submitcode.submission.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResultDTO {
    private Submission.EStatus status;
    private boolean isAccepted;
    private String message;

    private List<TestCaseResultDTO> testCaseResultDTOS;

    private String passedTestcase;
    private String maxTestcase;

    private double runtime;
    private double memory;
}
