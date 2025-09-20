package com.carolin.libraryproject.admin;

import com.carolin.libraryproject.loan.Loan;
import com.carolin.libraryproject.loan.LoanMapper;
import com.carolin.libraryproject.loan.LoanService;
import com.carolin.libraryproject.loan.loanDto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/loans")
@RestController
public class AdminLoanController {

    private LoanService loanService;
    private LoanMapper loanMapper;

    public AdminLoanController(LoanService loanService, LoanMapper loanMapper) {
        this.loanService = loanService;
        this.loanMapper = loanMapper;
    }


    @PostMapping
    public ResponseEntity<LoanDto> createLoan(@RequestParam Long userId, Long bookId) {

        Loan loan = loanService.createLoanAsAdmin(userId, bookId);
        LoanDto loanDto = loanMapper.toDto(loan);
        URI location = URI.create("/admin/loans/" + userId);
        return  ResponseEntity.created(location).body(loanDto);

    }


    @PutMapping("/return")
    public ResponseEntity<String> returnLoan(@RequestParam Long loanId) {

        loanService.returnBookAsAdmin(loanId);
        return  ResponseEntity.ok("Loan returnd by admin");

    }



    @PutMapping("/extend")
    public ResponseEntity<String> extendLoan(@RequestParam Long loanId) {

        loanService.extendBookAsAdmin(loanId);
        return  ResponseEntity.ok("Loan extend by admin");

    }




    @GetMapping("/user")
    public ResponseEntity<List<LoanDto>> getUserLoans(@RequestParam Long userId) {

        List<LoanDto> loans = loanService.findUserLoansAsAdmin(userId);
        return ResponseEntity.ok(loans);
    }





    @GetMapping
    public ResponseEntity<List<LoanDto>> getLoans() {

        List<LoanDto> loans = loanService.findAllLoans();
        return ResponseEntity.ok(loans);

    }



    @DeleteMapping
    public ResponseEntity<String> deleteLoan(Long loanId){
        loanService.deleteLoan(loanId);
        return ResponseEntity.noContent().build();
    }

}
