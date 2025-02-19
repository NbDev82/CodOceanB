package com.example.codoceanb.survey.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AnswerDTO {
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
