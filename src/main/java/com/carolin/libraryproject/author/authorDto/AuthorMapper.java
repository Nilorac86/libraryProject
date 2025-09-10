package com.carolin.libraryproject.author.authorDto;

import com.carolin.libraryproject.author.Author;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    // Mapper författare
    public AuthorDto toDto(Author author) {

        if (author == null) {
            return null;
        }

        return new AuthorDto(
                author.getFirstname() + " " + author.getLastname(),
                author.getBirthYear(),
                author.getNationality());

    }


    // Mapper författare till lista genom stream
    List<AuthorDto> toDtoList(List<Author> authors) {
        if (authors == null) {
            return Collections.emptyList();
        }

        return authors.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
