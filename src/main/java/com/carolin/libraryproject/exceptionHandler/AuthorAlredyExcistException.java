package com.carolin.libraryproject.exceptionHandler;

public class AuthorAlredyExcistException extends RuntimeException {

    public AuthorAlredyExcistException(String message) {
        super(message);

    }
}
