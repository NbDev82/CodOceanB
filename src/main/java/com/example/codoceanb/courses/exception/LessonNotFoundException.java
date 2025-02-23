package com.example.codoceanb.courses.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LessonNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public LessonNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
