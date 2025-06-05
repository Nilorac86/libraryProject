package com.carolin.libraryproject.authors;

import com.carolin.libraryproject.exceptionHandler.AuthorAlreadyExcistException;
import com.carolin.libraryproject.exceptionHandler.NoAuthorFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Optional<Author> optionalAuthor = authorRepository
                .findAuthorByFirstnameAndLastnameIgnoreCase(author.getFirstname(), author.getLastname());

        if (optionalAuthor.isPresent()) {
            throw new AuthorAlreadyExcistException("Author with name: " + author.getFirstname()+ " "
                    + author.getLastname() + " already exists");
        }

        return authorRepository.save(author);
    }
}

