package com.carolin.libraryproject.user.userDto;

import java.time.LocalDateTime;

public class UserDto {

    private String fullName;
    private String email;
    private LocalDateTime registrationDate;

    public UserDto() {
    }

    public UserDto( String fullName, String email, LocalDateTime registrationDate) {
        this.fullName = fullName;
        this.email = email;
        this.registrationDate = registrationDate;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }


}
