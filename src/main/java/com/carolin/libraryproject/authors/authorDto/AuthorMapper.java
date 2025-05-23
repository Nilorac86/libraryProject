package com.carolin.libraryproject.authors.authorDto;

import com.carolin.libraryproject.authors.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorDto toAuthorDto(Author author) {
        if (author == null) {
            return null;
        }

        return new AuthorDto(
                author.getFirstname(),
                author.getLastname(),
                author.getBirthYear(),
                author.getNationality());

    }


}
