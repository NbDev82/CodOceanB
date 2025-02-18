package com.example.codoceanb.submitcode.exceptionhandler;

import com.example.codoceanb.submitcode.exception.CompilationErrorException;
import com.example.codoceanb.submitcode.exception.SyntaxErrorException;
import com.example.codoceanb.submitcode.exception.TimeLimitedException;
import com.example.codoceanb.submitcode.exception.UnsupportedLanguageException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SubmitCodeExceptionHandler {

    private static final Logger log = LogManager.getLogger(SubmitCodeExceptionHandler.class);

    @ExceptionHandler(CompilationErrorException.class)
    public ResponseEntity<ErrorResponse> handleCompilationError(CompilationErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), LocalDateTime.now());
        log.error("Compile error: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(UnsupportedLanguageException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedLanguage(UnsupportedLanguageException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage(), LocalDateTime.now());
        log.error("Language unsupported: ", e);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(errorResponse);
    }

    @ExceptionHandler(TimeLimitedException.class)
    public ResponseEntity<ErrorResponse> handleTimeLimited(TimeLimitedException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, e.getMessage(), LocalDateTime.now());
        log.error("Time limited: ", e);
        return ResponseEntity.status(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
                .body(errorResponse);
    }

    @ExceptionHandler(SyntaxErrorException.class)
    public ResponseEntity<ErrorResponse> handleTimeLimited(SyntaxErrorException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), LocalDateTime.now());
        log.error("Syntax error: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleTimeLimited(NullPointerException e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now());
        log.error("Null pointer: ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }
}
