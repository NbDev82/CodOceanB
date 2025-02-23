package com.example.codoceanb.courses.exceptionhandler;

import com.example.codoceanb.courses.exception.CourseNotFoundException;
import com.example.codoceanb.courses.exception.CourseNotPublicException;
import com.example.codoceanb.infras.exeptionhandler.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CourseExceptionHandler {
    
    private static final Logger log = LogManager.getLogger(CourseExceptionHandler.class);
    
    @ExceptionHandler({CourseNotFoundException.class, CourseNotPublicException.class})
    public ResponseEntity<Object> handleCustomException(CourseNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(),
                ex.getMessage(), LocalDateTime.now());
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }
}
