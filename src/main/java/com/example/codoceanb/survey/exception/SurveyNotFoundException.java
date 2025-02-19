package com.example.codoceanb.survey.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SurveyNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public SurveyNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
