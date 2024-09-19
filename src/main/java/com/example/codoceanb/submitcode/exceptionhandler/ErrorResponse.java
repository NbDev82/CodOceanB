package com.example.codoceanb.submitcode.exceptionhandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(HttpStatus status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
