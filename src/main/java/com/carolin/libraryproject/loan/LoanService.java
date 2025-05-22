package com.carolin.libraryproject.loan;

import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.book.BookRepository;
import com.carolin.libraryproject.users.User;
import com.carolin.libraryproject.users.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanService {

    private LoanRepository loanRepository;

    private BookRepository bookRepository;

    private UserRepository userRepository;


    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;

    }

    public List<Loan> findUserLoans (Long userId) {
        return loanRepository.findByUserId(userId);
    }

    public Loan addLoan(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);

        return loanRepository.save(loan);
    }

    public void returnBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException ("Book already returned");
        }

        loan.setReturnedDate(LocalDateTime.now());

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() +1);

        bookRepository.save(book);
        loanRepository.save(loan);

    }

    public void extendBook (Long loanId){

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getReturnedDate() != null){
            throw new IllegalStateException ("Book already returned and cannot be extended");
        }

        LocalDateTime newReturnDate = LocalDateTime.now().plusDays(+ 14);

        loan.setDueDate(newReturnDate);
        loanRepository.save(loan);
    }

}
