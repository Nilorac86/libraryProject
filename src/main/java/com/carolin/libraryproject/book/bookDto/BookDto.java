package com.carolin.libraryproject.book.bookDto;

import com.carolin.libraryproject.author.authorDto.AuthorDto;
import com.carolin.libraryproject.author.authorDto.AuthorMapper;
import com.carolin.libraryproject.book.Book;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BookDto {
    private String title;
    private int publicationYear;
    private int availableCopies;
    private AuthorDto author;

    public BookDto() {
    }


    public BookDto(String title, int publicationYear, int availableCopies, AuthorDto author) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    @Component
    public static class BookMapper {

        private AuthorMapper authorMapper;

        public BookMapper( AuthorMapper authorMapper) {
            this.authorMapper = authorMapper;
        }

        public BookDto toDto(Book book) {

            if (book == null) {
                return null;
            }

            return new BookDto(
                    book.getTitle(),
                    book.getPublicationYear(),
                    book.getAvailableCopies(),
                    authorMapper.toDto(book.getAuthor()));

        }


        public List<BookDto> toDtoList(List<Book> books) {
            if (books == null) {
                return Collections.emptyList();
            }

            return books.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

        }
    }
}