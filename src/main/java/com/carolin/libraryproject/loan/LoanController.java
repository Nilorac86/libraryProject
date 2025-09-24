package com.carolin.libraryproject.loan;

import com.carolin.libraryproject.loan.loanDto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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


    // Skapa ett nytt lån med användarid och bokid som parameter. LoanDto response utan password.
    @PreAuthorize("hasAnyRole ('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<LoanDto> createLoan(Authentication authentication,
                                              @RequestParam Long bookId,
                                              @RequestParam(required = false) Long userId) {

        Loan loan = loanService.createLoan(authentication, bookId, userId);
        LoanDto loanDto = loanMapper.toDto(loan);
        URI location = URI.create("/loans/" + loan.getId());

        return ResponseEntity.created(location).body(loanDto);
    }


    // Återlämna lånad bok via lånid i sökväg
    @PreAuthorize("hasAnyRole ('ADMIN', 'USER')")
    @PutMapping("/return")
    public ResponseEntity<String> returnLoan(@RequestParam Long loanId, Authentication authentication) {

        if (loanId == null || loanId <= 0) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }
        loanService.returnBook(loanId, authentication);

        return ResponseEntity.ok("Book returned");
    }


    // Förlänga lån av bok via lånid i sökväg
    @PreAuthorize("hasAnyRole ('ADMIN', 'USER')")
    @PutMapping("/extend")
    public ResponseEntity<String> extendLoan(@RequestParam Long loanId, Authentication authentication) {

        if (loanId == null || loanId <= 0) {
            return ResponseEntity.badRequest().body("Id must be a positive integer");
        }
        loanService.extendBook(loanId, authentication);
        return ResponseEntity.ok("Book extended");
    }




    // Se alla sina lån
    @PreAuthorize("hasAnyRole ('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<LoanDto>> getLoans(Authentication authentication, Long userId) {

        List<LoanDto> loans = loanService.findUserLoans(authentication, userId);
        return ResponseEntity.ok(loans);

    }



    @PreAuthorize("hasRole ('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<LoanDto>> getLoans() {

        List<LoanDto> loans = loanService.findAllLoans();
        return ResponseEntity.ok(loans);

    }

    @PreAuthorize("hasRole ('ADMIN')")
    @DeleteMapping
    public ResponseEntity<String> deleteLoan(Long loanId){
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }


}
