package com.carolin.libraryproject.exceptionHandler;

public class LoanExpiredException extends RuntimeException {

    public LoanExpiredException() {
        super();

    }

    public LoanExpiredException(String s) {
        super(s);
    }
}
