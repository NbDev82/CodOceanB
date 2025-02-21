package com.example.codoceanb.survey.controller;

import com.example.codoceanb.survey.dto.SurveyDTO;
import com.example.codoceanb.survey.dto.SurveyQuestionDTO;
import com.example.codoceanb.survey.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SurveyQuestionDTO>> getFormSurvey() {
        return ResponseEntity.ok(surveyService.getQuestionList());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> saveSurvey(@RequestBody SurveyDTO survey,
                                              @RequestHeader(value = "Authorization") String authHeader) {
        return ResponseEntity.ok(surveyService.saveSurvey(survey, authHeader));
    }

    @PostMapping("/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> addSurveyQuestion(@RequestBody SurveyQuestionDTO question) {
        return ResponseEntity.ok(surveyService.addQuestion(question));
    }

    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteSurveyQuestion(@PathVariable UUID id) {
        return ResponseEntity.ok(surveyService.deleteQuestion(id));
    }

    @PutMapping("/questions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> updateSurveyQuestion(@PathVariable UUID id, @RequestBody SurveyQuestionDTO question) {
        return ResponseEntity.ok(surveyService.updateQuestion(id, question));
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSurveyStatistics() {
        return ResponseEntity.ok(surveyService.getStatistics());
    }
}
