package com.example.codoceanb.submitcode.exception;

public class TimeLimitedException extends RuntimeException{
    public TimeLimitedException(String message){
        super(message);
    }
}
