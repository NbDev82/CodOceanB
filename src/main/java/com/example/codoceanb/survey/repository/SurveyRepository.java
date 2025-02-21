package com.example.codoceanb.survey.repository;

import com.example.codoceanb.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyRepository extends JpaRepository<Survey, UUID> {
}
