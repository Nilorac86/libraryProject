package com.carolin.libraryproject.exceptionHandler;

public class NoAvailableCopiesException extends RuntimeException{


    public NoAvailableCopiesException(String message) {
        super(message);
    }
}
