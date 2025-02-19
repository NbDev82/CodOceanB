package com.example.codoceanb.survey.service;

import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.auth.exception.UnauthorizedException;
import com.example.codoceanb.auth.service.UserService;
import com.example.codoceanb.infras.externalapi.dto.ExternalApiResponseDTO;
import com.example.codoceanb.infras.externalapi.service.ExternalApiService;
import com.example.codoceanb.survey.dto.AnswerDTO;
import com.example.codoceanb.survey.dto.SurveyDTO;
import com.example.codoceanb.survey.dto.SurveyQuestionDTO;
import com.example.codoceanb.survey.entity.Survey;
import com.example.codoceanb.survey.entity.SurveyQuestion;
import com.example.codoceanb.survey.exception.ExternalServiceException;
import com.example.codoceanb.survey.repository.SurveyQuestionRepository;
import com.example.codoceanb.survey.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService{

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private ExternalApiService externalApiService;

    @Autowired
    private UserService userService;

    @Override
    public List<SurveyQuestionDTO> getQuestionList() {
        return surveyQuestionRepository.findAll()
                .stream()
                .map(SurveyQuestion::toDTO)
                .toList();
    }

    @Override
    public Boolean saveSurvey(SurveyDTO survey, String authHeader) {
        User user = userService.getUserDetailsFromToken(authHeader);
        if (user == null) {
            throw new UnauthorizedException("User is not logged in or session has expired.");
        }

        ExternalApiResponseDTO resultOfSurvey;
        try {
            resultOfSurvey = externalApiService.getExternalData("/api/v1/userInsight");
        } catch (ExternalServiceException e) {
            throw new ExternalServiceException("Unable to connect to the evaluation system. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(resultOfSurvey == null) {
            return false;
        }

        Survey s = Survey.builder()
                .surveyData(survey.getSurveyQuestionDTOs()
                        .stream()
                        .map(s1 -> s1.getQuestion() + ":" + s1.getSelectedAnswer().getAnswer())
                        .collect(Collectors.joining(";")))
                .user(user)
                .build();
        surveyRepository.save(s);

        user.setSurvey(s);
        userService.update(user);

        return true;
    }
    @Override
    public Boolean addQuestion(SurveyQuestionDTO question) {
        SurveyQuestion surveyQuestion = question.toEntity();
        surveyQuestionRepository.save(surveyQuestion);
        return true;
    }

    @Override
    public Boolean deleteQuestion(UUID id) {
        if (surveyQuestionRepository.existsById(id)) {
            surveyQuestionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateQuestion(UUID id, SurveyQuestionDTO question) {
        if (surveyQuestionRepository.existsById(id)) {
            SurveyQuestion surveyQuestion = surveyQuestionRepository.findById(id).orElseThrow();
            surveyQuestion.setQuestion(question.getQuestion());
            surveyQuestion.setAnswers(question.getAnswerDTOs()
                    .stream()
                    .map(AnswerDTO::toEntity)
                    .collect(Collectors.toList()));
            surveyQuestionRepository.save(surveyQuestion);
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> getStatistics() {
        return null;
        // List<Survey> surveys = surveyRepository.findAll();
        // Map<String, Long> questionStats = surveys.stream()
        //         .flatMap(survey -> Arrays.stream(survey.getSurveyData().split(";")))
        //         .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        // Map<String, Long> courseSuggestionStats = surveys.stream()
        //         .flatMap(survey -> Arrays.stream(survey.getSurveyData().split(";")))
        //         .filter(data -> data.contains("course"))
        //         .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        // return Map.of(
        //         "totalSurveys", surveys.size(),
        //         "questionStats", questionStats,
        //         "courseSuggestionStats", courseSuggestionStats
        // );
    }
}
