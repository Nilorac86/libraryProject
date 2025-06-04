package com.carolin.libraryproject;

import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.book.BookRepository;
import com.carolin.libraryproject.loan.Loan;
import com.carolin.libraryproject.loan.LoanRepository;
import com.carolin.libraryproject.loan.LoanService;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class) // Aktiverar mockito, mock och injectmock fungerar utan spring
public class loanServiceUnitTest {

    // Skapar en fejk version av databas
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;

    //Skapar en riktig LoanService men implementerar mina mockade repo istället
    @InjectMocks
    private LoanService loanService;

    // Initierar mockarna innan varje test
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void unitTestLoanService_createLoan(){

        // Arrange - Förbereder testdata och mockning
        Long userId = 1L;
        Long bookId = 1L;

        User user = new User();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);
        book.setAvailableCopies(3);
        book.setTitle("Test Book");

        Loan loan = new Loan();
        loan.setId(10L);
        loan.setUser(user);
        loan.setBook(book);


        // Definierar hur mockobjekten ska bete sig under testet.
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        //Act - Kör metoden som ska testas

        Loan createdLoan = loanService.createLoan(userId, bookId);

        //Assert - Kontroll att metoden fungerar som den ska.

        assertNotNull(createdLoan);
        assertEquals(10L, createdLoan.getId());
        assertEquals(user, createdLoan.getUser());
        assertEquals(book, createdLoan.getBook());
        assertEquals(2, book.getAvailableCopies());

    }

}
