package com.example.codoceanb.submitcode.exception;

public class CompilationErrorException extends RuntimeException{
    public CompilationErrorException(String message){
        super(message);
    }
}
