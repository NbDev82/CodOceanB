package com.example.codoceanb.survey.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SurveyQuestionDTO {
    private String question;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AnswerDTO> answerDTOs;
    private AnswerDTO selectedAnswer;
}
