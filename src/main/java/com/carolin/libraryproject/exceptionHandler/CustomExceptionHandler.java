package com.carolin.libraryproject.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());}

    
    @ExceptionHandler(NoLoanFoundException.class)
    public ResponseEntity<String> handleNoLoanFound(NoLoanFoundException ex){
        log.warn("Loan not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());}



    @ExceptionHandler(AuthorAlreadyExcistException.class)
    public ResponseEntity<String> handleAuthorExist(AuthorAlreadyExcistException ex){
        log.warn("Author already exist: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FOUND).body(ex.getMessage());}

    @ExceptionHandler(BookAlreadyExcistException.class)
    public ResponseEntity<String> handleBookExist(BookAlreadyExcistException ex){
        log.warn("Book already exist: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FOUND).body(ex.getMessage());}


    @ExceptionHandler(EmailAlreadyExcistException.class)
    public ResponseEntity<String> handleEmailExist(EmailAlreadyExcistException ex){
        log.warn("User with this email already exist: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Email already exist");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse.toString());}


    @ExceptionHandler(NotValidPasswordException.class)
    public ResponseEntity<String> handleNotValidPassword(NotValidPasswordException ex){
        log.warn("Password not valid : {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Password must be at least 8 characters, " +
                "including one digit and one uppercase letter");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse.toString());}

    @ExceptionHandler(NotValidEmailException.class)
    public ResponseEntity<Map<String, String>> handleNotValidEmail(NotValidEmailException ex) {
        log.warn("Email not valid: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of(
                "error", "Email is not in a valid format"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}



