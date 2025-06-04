package com.carolin.libraryproject.exceptionHandler;

public class NoAuthorFoundException extends RuntimeException {
    public NoAuthorFoundException(String message) {
        super(message);
    }
}
