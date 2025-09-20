package com.carolin.libraryproject.author;

import com.carolin.libraryproject.author.authorDto.AuthorMapper;
import com.carolin.libraryproject.author.authorDto.AuthorRequestDto;
import com.carolin.libraryproject.exceptionHandler.AuthorAlreadyExcistException;
import com.carolin.libraryproject.exceptionHandler.NoAuthorFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;


    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
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
            throw new NoAuthorFoundException("No author found");
        }

        return authors;
    }

    public Author addAuthor(AuthorRequestDto authorRequestDto) {

        Author author = authorMapper.authorToEntity(authorRequestDto);

        Optional<Author> optionalAuthor = authorRepository
                .findAuthorByFirstnameAndLastnameIgnoreCase(author.getFirstname(), author.getLastname());

        if (optionalAuthor.isPresent()) {
            throw new AuthorAlreadyExcistException("Author already exists");
        }

        return authorRepository.save(author);
    }


    public void deleteAuthor(Long authorId) {

        if (!authorRepository.existsById(authorId)) {
            throw new NoAuthorFoundException("No author found");
        }
        authorRepository.deleteById(authorId);
    }
}

