package com.example.codoceanb.survey.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SurveyParsingException extends RuntimeException {
    private final HttpStatus status;

    public SurveyParsingException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
