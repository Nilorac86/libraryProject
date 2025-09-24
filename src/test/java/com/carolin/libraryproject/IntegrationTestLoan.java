package com.carolin.libraryproject;

import com.carolin.libraryproject.authentication.LoginAttemptService;
import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.book.BookRepository;
import com.carolin.libraryproject.exceptionHandler.NoAvailableCopiesException;
import com.carolin.libraryproject.loan.Loan;
import com.carolin.libraryproject.loan.LoanService;
import com.carolin.libraryproject.security.CustomUserDetails;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    // Metod för att skapa användare
    private User createTestUser() {
        String uniqueEmail = "test" + UUID.randomUUID() + "@example.com";
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail(uniqueEmail);
        user.setPassword("password");
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    // Metod för att skapa bok
    private Book createTestBook(int availableCopies) {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAvailableCopies(availableCopies);
        book.setTotalCopies(availableCopies);
        return bookRepository.save(book);
    }



    @Test
    @WithUserDetails("test@example.com")
    @Transactional
    void testToCreateLoanWithMockMvc() throws Exception {
        User user = createTestUser();
        Book book = createTestBook(3);

        mockMvc.perform(post("/loans")
                        .param("bookId", String.valueOf(book.getId()))
                        .param("targetUserId", String.valueOf(user.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.borrowedDate").value(startsWith(LocalDate.now().toString())));
    }


    @Transactional
    @Test
    void testLoanSetsCorrectDueDate() {
        // Arrange
        User user = createTestUser();
        Book book = createTestBook(3);

        LoginAttemptService loginAttemptService = new LoginAttemptService(mock(UserRepository.class));
        CustomUserDetails userDetails = new CustomUserDetails(user, loginAttemptService);

        // Sätt SecurityContext
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        Loan result = loanService.createLoan(auth, book.getId(), null);

        // Assert
        LocalDate expectedDueDate = LocalDate.now().plusDays(14);
        assertEquals(expectedDueDate, result.getDueDate().toLocalDate());

        // Rensa SecurityContext efter testet
        SecurityContextHolder.clearContext();
    }



    @Transactional
    @Test
    void testCannotCreateLoanIfNoAvailableCopies() {
        // Arrange
        User user = createTestUser();
        Book book = createTestBook(0);

        // Skapa CustomUserDetails med LoginAttemptService
        LoginAttemptService loginAttemptService = new LoginAttemptService(mock(UserRepository.class));
        CustomUserDetails userDetails = new CustomUserDetails(user, loginAttemptService);


        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act & Assert
        assertThrows(NoAvailableCopiesException.class, () ->
                loanService.createLoan(SecurityContextHolder.getContext().getAuthentication(),
                        book.getId(), null));

        // Rensa SecurityContext efter testet
        SecurityContextHolder.clearContext();
    }

}






