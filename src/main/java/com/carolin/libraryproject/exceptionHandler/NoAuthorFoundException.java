package com.carolin.libraryproject.exceptionHandler;

public class NoAuthorFoundException extends Throwable {
    public NoAuthorFoundException(String message) {
        super(message);
    }
}
