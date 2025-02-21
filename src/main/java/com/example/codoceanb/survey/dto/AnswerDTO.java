package com.example.codoceanb.survey.dto;

import com.example.codoceanb.survey.entity.Answer;
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

    public Answer toEntity() {
        return Answer.builder()
                .answer(this.answer)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
