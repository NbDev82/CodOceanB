package com.example.codoceanb.courses.exceptionhandler;

import com.example.codoceanb.courses.exception.LessonNotFoundException;
import com.example.codoceanb.infras.exeptionhandler.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class LessonExceptionHandler {
    private static final Logger log = LogManager.getLogger(LessonExceptionHandler.class);

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<Object> handleLessonNotFoundException(LessonNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(),
                ex.getMessage(), LocalDateTime.now());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}
