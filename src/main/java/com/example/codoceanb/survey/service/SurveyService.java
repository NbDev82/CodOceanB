package com.example.codoceanb.survey.service;


import com.example.codoceanb.survey.dto.SurveyDTO;
import com.example.codoceanb.survey.dto.SurveyQuestionDTO;

import java.util.List;

public interface SurveyService {
    List<SurveyQuestionDTO> getQuestionList();

    Boolean saveSurvey(SurveyDTO survey, String authHeader);
}
