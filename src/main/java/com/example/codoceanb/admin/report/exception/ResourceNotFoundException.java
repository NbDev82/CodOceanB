package com.example.codoceanb.admin.report.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Can't find out violation item!");
    }
}
