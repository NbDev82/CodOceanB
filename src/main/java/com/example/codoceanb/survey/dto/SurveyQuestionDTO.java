package com.example.codoceanb.survey.dto;

import com.example.codoceanb.survey.entity.SurveyQuestion;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public SurveyQuestion toEntity() {
        return SurveyQuestion.builder()
                .question(question)
                .answers(answerDTOs
                        .stream()
                        .map(AnswerDTO::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
