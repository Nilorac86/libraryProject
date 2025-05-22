package com.carolin.libraryproject.loan;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;

    }

    @PostMapping
    public Loan addLoan(@RequestParam Long userId, @RequestParam Long bookId) {
        return loanService.addLoan(userId, bookId);
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
