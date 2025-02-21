package com.example.codoceanb.courses.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CourseNotFoundException extends RuntimeException {
    private final HttpStatus status;

    public CourseNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
