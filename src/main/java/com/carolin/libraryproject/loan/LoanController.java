package com.carolin.libraryproject.loan;

import com.carolin.libraryproject.loan.loanDto.LoanDto;
import com.carolin.libraryproject.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final LoanMapper loanMapper;

    public LoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;

    }

    @GetMapping
    public ResponseEntity<List<LoanDto>> getLoans() {

        List<LoanDto> loans = loanService.findAllLoans();
        return ResponseEntity.ok(loans);

    }


    // Skapa ett nytt lån med användarid och bokid som parameter. LoanDto response utan password.
    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@AuthenticationPrincipal CustomUserDetails loggedInUser, @RequestParam Long bookId) {



        Loan loan = loanService.createLoan(loggedInUser.getUser(), bookId);
        LoanDto loanDto = loanMapper.toDto(loan);
        URI location = URI.create("/loans/" + loan.getId());

        return ResponseEntity.created(location).body(loanDto);
    }


    // Återlämna lånad bok via lånid i sökväg
    @PutMapping("/{id}/return")
    public ResponseEntity<String> returnLoan(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails loggedInUser ) {

        if(id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }

        loanService.returnBook(id, loggedInUser.getUser());
        return ResponseEntity.ok("Book returned");
    }


    // Förlänga lån av bok via lånid i sökväg
    @PutMapping("/{id}/extend")
    public ResponseEntity<String> extendLoan(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails loggedInUser) {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }
        loanService.extendBook(id, loggedInUser.getUser());
        return ResponseEntity.ok("Book extended");
    }
}
