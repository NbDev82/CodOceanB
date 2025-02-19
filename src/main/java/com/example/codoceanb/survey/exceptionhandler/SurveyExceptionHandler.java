package com.example.codoceanb.survey.exceptionhandler;

import com.example.codoceanb.infras.exeptionhandler.ErrorResponse;
import com.example.codoceanb.survey.exception.ExternalServiceException;
import com.example.codoceanb.survey.exception.SurveyNotFoundException;
import com.example.codoceanb.survey.exception.SurveyParsingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class SurveyExceptionHandler {

    private static final Logger log = LogManager.getLogger(SurveyExceptionHandler.class);

    @ExceptionHandler({ExternalServiceException.class, SurveyParsingException.class})
    public ResponseEntity<Object> handleCustomException(ExternalServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getMessage(), LocalDateTime.now());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(SurveyNotFoundException.class)
    public ResponseEntity<Object> handleCustomException(SurveyNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(),
                ex.getMessage(), LocalDateTime.now());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}
