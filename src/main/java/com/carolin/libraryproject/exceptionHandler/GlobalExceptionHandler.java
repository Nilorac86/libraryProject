package com.carolin.libraryproject.exceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;

import javax.security.auth.login.AccountLockedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ################# CUSTOM EXCEPTION HANDLERS ###########################################

    // 403 Forbidden för permanent låst konto
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String, String>> handleAccountLocked(AccountLockedException ex) {
        log.warn("Account locked: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Invalid login attempt: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Failed to login");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    // 400 Bad request
    @ExceptionHandler(NoAvailableCopiesException.class)
    public ResponseEntity<Map<String, String>> handleNoCopies(NoAvailableCopiesException ex) {
        log.warn("No copies available: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "No copies available");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    // 404 Not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleEntityNotFound(EntityNotFoundException ex){
    log.warn("Entity not found: {}", ex.getMessage());
    Map<String, String> errorResponse = Map.of("error", "Entity not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);}



    // 404 Not found
    @ExceptionHandler(NoAuthorFoundException.class)
        public ResponseEntity<Map<String, String>> handleNoAuthor(NoAuthorFoundException ex){
        log.warn("No author found: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "No author found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    // 404 Not found
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex){
        log.warn("User not found: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "User not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }



    // 400 Bad request
    @ExceptionHandler(LoanExpiredException.class)
    public ResponseEntity<Map<String, String>> handleLoanExpired(LoanExpiredException ex){
        log.warn("Loan dueDate has passed: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Loan expired");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }



    // 404 Not found
    @ExceptionHandler(NoLoanFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoLoanFound(NoLoanFoundException ex){
        log.warn("Loan not found: {}", ex.getMessage());
        Map<String, String> errorRespone = Map.of("error", "Loan not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorRespone);
    }


    // 409 Conflict
    @ExceptionHandler(AuthorAlreadyExcistException.class)
    public ResponseEntity <Map<String, String>> handleAuthorExist(AuthorAlreadyExcistException ex){
        log.warn("Author already exist: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Author already exist");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }



    // 409 Conflict
    @ExceptionHandler(BookAlreadyExcistException.class)
    public ResponseEntity<Map<String, String>> handleBookExist(BookAlreadyExcistException ex){
        log.warn("Book already exist: {}", ex.getMessage());
        Map<String , String > errorResponse = Map.of("error", "Book already exist");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }



    // 409 Conflict
    @ExceptionHandler(EmailAlreadyExcistException.class)
    public ResponseEntity<Map<String, String>> handleEmailExist(EmailAlreadyExcistException ex){
        log.warn("User with this email already exist: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Email already exist");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(TemporarilyBlockedException.class)
    public ResponseEntity<Map<String, String>> handleTemporarilyBlocked(TemporarilyBlockedException ex) {
        log.warn("Temporarily blocked account: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<Map<String, String>> handleTooManyRequests(TooManyRequestsException ex) {
        log.warn("Too many requests from IP: {}", ex.getMessage());
        return ResponseEntity.status(429).body(Map.of("error", ex.getMessage()));
    }




// ########################### GENERELLA EXCEPTIONS ######################################

    // 403 när användaren är blockad pga för många inloggnings försök.
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, String>> handleDisabledUser(DisabledException ex) {
        Map<String, String> errorResponse = Map.of(
                "error","Your account is temporarily locked due to too many failed login attempts.");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }



    // 403 forbidden. Användaren saknar behörighet
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }



    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        Map<String, String> errorResponse = Map.of("error", "Resource not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


// 500 internal server error. Ger oväntat server fel. Fångar alla okända fel
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherErrors(Exception ex){
        log.error("Unexpected error occurred", ex);
        Map<String, String> errorResponse = Map.of("error", "Unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    // Fångar valideringsfel och mappar dessa
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


}



