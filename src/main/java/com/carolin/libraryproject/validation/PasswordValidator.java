package com.carolin.libraryproject.validation;

public class PasswordValidator {

    public static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";

    public static boolean isPasswordValid (String password) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}
