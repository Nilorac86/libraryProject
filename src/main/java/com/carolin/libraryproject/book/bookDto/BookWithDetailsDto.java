package com.carolin.libraryproject.book.bookDto;

import com.carolin.libraryproject.authors.authorDto.AuthorDto;

public class BookWithDetailsDto {
    private String title;
    private String publicationYear;
    private int availableCopies;
    private AuthorDto author;

    public BookWithDetailsDto() {
    }

    public BookWithDetailsDto(String title, String publicationYear, int availableCopies, AuthorDto author) {
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

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
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
}