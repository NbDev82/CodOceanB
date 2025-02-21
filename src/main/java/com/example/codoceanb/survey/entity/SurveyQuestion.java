package com.example.codoceanb.survey.entity;

import com.example.codoceanb.survey.dto.SurveyQuestionDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "survey_question")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "surveyQuestion", cascade = CascadeType.ALL)
    private List<Answer> answers;

    public SurveyQuestionDTO toDTO() {
        return SurveyQuestionDTO.builder()
                .question(this.question)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .answerDTOs(this.answers != null ? this.answers.stream()
                        .map(Answer::toDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }
}
