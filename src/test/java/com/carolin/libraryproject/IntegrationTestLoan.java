package com.carolin.libraryproject;

import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.book.BookRepository;
import com.carolin.libraryproject.exceptionHandler.NoAvailableCopiesException;
import com.carolin.libraryproject.loan.Loan;
import com.carolin.libraryproject.loan.LoanService;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;


import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTestLoan {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LoanService loanService;

    //Metod för att skapa användare i varje testklass
    private User createTestUser() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@example.com";
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail(uniqueEmail);
        user.setPassword("password");
        return userRepository.save(user);
    }

    //Metod för att skapa bok i varje testklass

    private Book createTestBook(int availableCopies) {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAvailableCopies(availableCopies);
        book.setTotalCopies(availableCopies);
        return bookRepository.save(book);
    }



    // Kontrollerar att ett lån skapas korrekt
    @Transactional
    @Test
    void testToCreateLoanWithParamSholudReturnLoan(){

        // Arrange - skapa testdata i DB

        User user = createTestUser();
        Book book = createTestBook(3);


        // Act & Assert - kör request och kontrollera svar
        try {
            mockMvc.perform(post("/loans")
                            .param("userId", String.valueOf(user.getId()))
                            .param("bookId", String.valueOf(book.getId()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.user.id").value(user.getId()))
                    .andExpect(jsonPath("$.book.id").value(book.getId()))
                    .andExpect(jsonPath("$.borrowedDate").value(startsWith(LocalDate.now().toString())));
            ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // Test som kontrollerar att dueDate är 14 dagar efter låndatum

    @Test
    void testLoanSetsCorrectDueDate() throws Exception {
        // Arrange - skapa testdata i DB

        User user = createTestUser();
        Book book = createTestBook(3);

        // Act
        Loan result = loanService.createLoan(user.getId(), book.getId());

        //Assert

        LocalDate excepectedDueDate = LocalDate.now().plusDays(14);
        assertEquals(excepectedDueDate, result.getDueDate().toLocalDate());

    }


    // Test som kontrollerar att om det inte finns några tillgängliga böcker kan lån inte slutföras.
    @Test
    void testCannotCreateLoanIfNoAvailableCopies()  {
        // Arrange - skapa testdata i DB

        User user = createTestUser();
        Book book = createTestBook(0);

        // Act & Assert
        assertThrows(NoAvailableCopiesException.class, () ->{
            loanService.createLoan(1L, book.getId());
        });

    }

}



