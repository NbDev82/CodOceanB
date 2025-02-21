package com.example.codoceanb.survey.entity;

import com.example.codoceanb.survey.dto.AnswerDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "answers")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "survey_question_id")
    private SurveyQuestion surveyQuestion;

    public AnswerDTO toDTO() {
        return AnswerDTO.builder()
                .answer(this.answer)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
