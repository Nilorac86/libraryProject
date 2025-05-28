package com.carolin.libraryproject.authors.authorDto;

import com.carolin.libraryproject.authors.Author;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author author) {

        if (author == null) {
            return null;
        }

        return new AuthorDto(
                author.getFirstname() + " " + author.getLastname(),
                author.getBirthYear(),
                author.getNationality());

    }

    List<AuthorDto> toDtoList(List<Author> authors) {
        if (authors == null) {
            return Collections.emptyList();
        }

        return authors.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
