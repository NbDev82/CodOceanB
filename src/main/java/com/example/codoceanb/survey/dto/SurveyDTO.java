package com.example.codoceanb.survey.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SurveyDTO {
    private List<SurveyQuestionDTO> surveyQuestionDTOs;
}
