package com.carolin.libraryproject.exceptionHandler;

public class BookAlreadyExcistException extends RuntimeException {

    public BookAlreadyExcistException(String message) {
        super(message);

    }
}
