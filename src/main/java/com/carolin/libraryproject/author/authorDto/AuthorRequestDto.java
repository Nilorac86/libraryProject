package com.carolin.libraryproject.author.authorDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthorRequestDto {

    @NotBlank(message= "Förnamn får inte vara tomt")
    @Size(min = 2, max = 64, message = "Förnamnet måste vara mellan 2-64 tecken långt")
    private String firstName;

    @NotBlank(message= "Efternamn får inte vara tomt")
    @Size(min = 2, max = 64, message = "Efternamnet måste vara mellan 2-64 tecken långt")
    private String lastName;


    @Min(value = 1000, message = "Födelseår måste vara efter år 1000")
    @Max(value = 2025, message = "Födelseår kan inte vara i framtiden")
    private int birthYear;

    @Size(min = 2, max = 50)
    private String nationality;

    public AuthorRequestDto() {
    }

    public AuthorRequestDto(String firstName, String lastName, int birthYear, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public int getBirthYear() {

        return birthYear;
    }

    public void setBirthYear(int birthYear) {

        this.birthYear = birthYear;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {

        this.nationality = nationality;
    }

}
