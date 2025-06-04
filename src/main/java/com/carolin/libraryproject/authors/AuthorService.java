package com.carolin.libraryproject.authors;

import com.carolin.libraryproject.exceptionHandler.NoAuthorFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;

    }

    // Hämtar alla författare
    public List<Author> getAllAuthors() {
       List<Author> authors = authorRepository.findAll();
       return authors;
    }

    // Hämtar alla författare genom att söka på efternamn
    public List<Author> findByLastname (String lastname) {
        List<Author> authors = authorRepository.findAuthorsByLastnameIgnoreCase(lastname);

        if (authors.isEmpty()) {
            throw new NoAuthorFoundException("No author found with lastname: " + lastname);
        }

        return authors;
    }

    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }
}

