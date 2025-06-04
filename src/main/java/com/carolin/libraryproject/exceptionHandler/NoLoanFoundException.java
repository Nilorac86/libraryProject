package com.carolin.libraryproject.exceptionHandler;

public class NoLoanFoundException extends RuntimeException {

    public NoLoanFoundException(String message) {
        super(message);

    }
}
