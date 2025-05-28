package com.carolin.libraryproject.loan;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;

    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestParam Long userId, @RequestParam Long bookId) {

        if (userId == null || bookId == null) {
            return ResponseEntity.badRequest().build();

        }
        Loan loan = loanService.createLoan(userId, bookId);
        URI location = URI.create("/loans/" + loan.getId());

        return ResponseEntity.created(location).body(loan);
    }



    @PutMapping("/{id}/return")
    public ResponseEntity<String> returnLoan(@PathVariable Long id) {

        if(id == null) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }

        loanService.returnBook(id);
        return ResponseEntity.ok("Book returned");
    }


    @PutMapping("/{id}/extend")
    public ResponseEntity<String> extendLoan(@PathVariable Long id) {

        if (id == null) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }
        loanService.extendBook(id);
        return ResponseEntity.ok("Book extended");
    }
}
