package com.carolin.libraryproject.author.authorDto;


public class AuthorDto {
    private String fullName;
    private int birthYear;
    private String nationality;


    public AuthorDto() {
    }


    public AuthorDto(String fullName, int birthYear, String nationality) {
        this.fullName = fullName;
        this.birthYear = birthYear;
        this.nationality = nationality;

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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


