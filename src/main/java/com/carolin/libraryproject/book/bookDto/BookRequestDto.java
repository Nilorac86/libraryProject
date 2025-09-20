package com.carolin.libraryproject.book.bookDto;

import jakarta.validation.constraints.*;

public class BookRequestDto {


    @NotBlank(message = "Titel får inte vara tom")
    @Size( min = 1, max = 250)
    private String title;

    @Min(value = 1000, message = "Publiseringsår kan inte vara innan år 1000.")
    private int publicationYear;

    @Min(value = 1, message = " Minsta antal kopior måste vara 1")
    private int totalCopies;


    @Positive(message = "Författar ID måste vara ett positivt nummer")
    private  int authorId;


    public BookRequestDto() {
    }


    public BookRequestDto(String title, int publicationYear, int totalCopies, int authorId) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.authorId = authorId;
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

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
}
