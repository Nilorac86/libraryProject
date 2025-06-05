package com.carolin.libraryproject.exceptionHandler;

public class AuthorAlreadyExcistException extends RuntimeException {

    public AuthorAlreadyExcistException(String message) {
        super(message);

    }
}
