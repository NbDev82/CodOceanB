package com.example.codoceanb.survey.repository;

import com.example.codoceanb.survey.entity.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, UUID> {
}
