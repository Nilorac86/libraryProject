package com.carolin.libraryproject.user.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message= "Förnamn får inte vara tomt")
    @Size(min = 2, max = 64, message = "Förnamnet måste vara mellan 2-64 tecken långt")
    private String firstName;

    @NotBlank(message= "Efternamn får inte vara tomt")
    @Size(min = 2, max = 64, message = "Efternamnet måste vara mellan 2-64 tecken långt")
    private String lastName;


    @Email(message = "Ogiltig e-postadress")
    @Size(min = 3, max = 64, message = "E-posten måste vara mellan 3-64 tecken långt")
    private String email;


    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,64}$",
            message = "Lösenordet måste vara mellan 8  och 64 tecken och innehålla minst en stor och en liten bokstav")
    private String password;


    public UserRequestDto(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
