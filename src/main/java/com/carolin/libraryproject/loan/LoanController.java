package com.carolin.libraryproject.loan;

import com.carolin.libraryproject.loan.loanDto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;

    }

    // Skapa ett nytt lån med användarid och bokid som parameter
    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@RequestParam Long userId, @RequestParam Long bookId) {

        if (userId == null || bookId == null) {
            return ResponseEntity.badRequest().build();

        }
        Loan loan = loanService.createLoan(userId, bookId);
        LoanDto loanDto = loanMapper.toDto(loan);
        URI location = URI.create("/loans/" + loan.getId());

        return ResponseEntity.created(location).body(loanDto);
    }


    // Återlämna lånad bok via lånid i sökväg
    @PutMapping("/{id}/return")
    public ResponseEntity<String> returnLoan(@PathVariable Long id) {

        if(id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }

        loanService.returnBook(id);
        return ResponseEntity.ok("Book returned");
    }


    // Förlänga lån av bok via lånid i sökväg
    @PutMapping("/{id}/extend")
    public ResponseEntity<String> extendLoan(@PathVariable Long id) {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }
        loanService.extendBook(id);
        return ResponseEntity.ok("Book extended");
    }
}
