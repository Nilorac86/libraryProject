package com.carolin.libraryproject.exceptionHandler;

public class NotValidEmailException extends RuntimeException {
    public NotValidEmailException(String message) {
        super(message);
    }
}
