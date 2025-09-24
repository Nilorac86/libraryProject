package com.carolin.libraryproject.exceptionHandler;

public class TemporarilyBlockedException extends RuntimeException {
    public TemporarilyBlockedException(String message) {
        super(message);
    }
}
