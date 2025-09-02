package com.carolin.libraryproject.exceptionHandler;

public class NotValidPasswordException extends RuntimeException {

    public NotValidPasswordException(String message) {
        super(message);
    }
}
