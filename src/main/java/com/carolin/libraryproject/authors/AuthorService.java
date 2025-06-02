package com.carolin.libraryproject.authors;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;

    }

    public List<Author> getAllAuthors() {
       List<Author> authors = authorRepository.findAll();
       return authors;
    }

    public List<Author> findByLastname (String lastname) {
        List<Author> authors = authorRepository.findAuthorsByLastnameIgnoreCase(lastname);
        return authors;
    }

    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }
}

