package com.example.codoceanb.submitcode.DTO;

import com.example.codoceanb.submitcode.submission.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubmissionDTO {
    private Long id;
    private Submission.ELanguage language;
    private String codeSubmitted;
    private double score;
    private Submission.EStatus status;
    private String runtime;
    private String memory;
    private String createdAt;
}
