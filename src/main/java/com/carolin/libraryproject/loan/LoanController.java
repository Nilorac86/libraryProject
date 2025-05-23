package com.carolin.libraryproject.loan;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;

    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestParam Long userId, @RequestParam Long bookId) {
        Loan loan = loanService.createLoan(userId, bookId);
        return ResponseEntity.ok(loan);
    }

    @PutMapping("/{id}/return")
    public void returnLoan(@PathVariable Long id) {
        loanService.returnBook(id);
    }

    @PutMapping("/{id}/extend")
    public void extendLoan(@PathVariable Long id) {
        loanService.extendBook(id);
    }
}
