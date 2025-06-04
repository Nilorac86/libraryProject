package com.carolin.libraryproject.exceptionHandler;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
