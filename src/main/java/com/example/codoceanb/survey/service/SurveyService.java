package com.example.codoceanb.survey.service;


import com.example.codoceanb.survey.dto.SurveyDTO;
import com.example.codoceanb.survey.dto.SurveyQuestionDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SurveyService {
    List<SurveyQuestionDTO> getQuestionList();

    Boolean saveSurvey(SurveyDTO survey, String authHeader);

    Boolean addQuestion(SurveyQuestionDTO question);

    Boolean deleteQuestion(UUID id);

    Boolean updateQuestion(UUID id, SurveyQuestionDTO question);

    Map<String, Object> getStatistics();
}
