package com.carolin.libraryproject.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    // Olika exception handlers som returnerar en textsträng
    
    @ExceptionHandler(NoAvailableCopiesException.class)
    public ResponseEntity<String> handleNoCopies(NoAvailableCopiesException ex) {
        log.warn("No copies available: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex){
    log.warn("Entity not found: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());}


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherErrors(Exception ex){
    log.error("Unexpected error occurred", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(NoAuthorFoundException.class)
        public ResponseEntity<String>handleNoAuthor(NoAuthorFoundException ex){
        log.warn("No author found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex){
        log.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());}

    @ExceptionHandler(LoanExpiredException.class)
    public ResponseEntity<String> handleLoanExpired(LoanExpiredException ex){
        log.warn("Loan dueDate has passed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());}

    
    @ExceptionHandler(NoLoanFoundException.class)
    public ResponseEntity<String> handleNoLoanFound(NoLoanFoundException ex){
        log.warn("Loan not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());}
}
