package com.carolin.libraryproject.book.bookDto;

import com.carolin.libraryproject.author.authorDto.AuthorDto;


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

}