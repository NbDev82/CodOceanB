package com.example.codoceanb.survey.controller;

import com.example.codoceanb.survey.dto.SurveyDTO;
import com.example.codoceanb.survey.dto.SurveyQuestionDTO;
import com.example.codoceanb.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    public ResponseEntity<List<SurveyQuestionDTO>> getFormSurvey() {
        return ResponseEntity.ok(surveyService.getQuestionList());
    }

    @PostMapping
    public ResponseEntity<Boolean> saveSurvey(@RequestBody SurveyDTO survey,
                                              @RequestHeader(value = "Authorization") String authHeader) {
        return ResponseEntity.ok(surveyService.saveSurvey(survey, authHeader));
    }
}
