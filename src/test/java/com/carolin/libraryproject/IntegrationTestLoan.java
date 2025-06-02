package com.carolin.libraryproject;

import com.carolin.libraryproject.book.Book;
import com.carolin.libraryproject.book.BookRepository;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

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

    @Transactional
    @Test
    void testToCreateLoanWithParamSholudReturnLoan(){

        // Arrange - skapa testdata i DB

        String uniqueEmail = "test" + UUID.randomUUID() + "@example.com";

        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail(uniqueEmail);
        user.setPassword("password");
        user = userRepository.save(user);



        Book book = new Book();
        book.setTitle("Test Book");
        book.setAvailableCopies(3);
        book.setTotalCopies(3);
        book = bookRepository.save(book);

        // Act & Assert - kör request och kontrollera svar
        try {
            mockMvc.perform(post("/loans")
                            .param("userId", String.valueOf(user.getId()))
                            .param("bookId", String.valueOf(book.getId()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.user.id").value(user.getId()))
                    .andExpect(jsonPath("$.book.id").value(book.getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




}



