package com.lexis.nexis.poc.exception;

public class NoMatchingEmployeeException extends RuntimeException {
    public NoMatchingEmployeeException(String message) {
        super(message);
    }

}
