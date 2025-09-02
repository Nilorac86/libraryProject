package com.carolin.libraryproject.validation;

public class EmailValidator {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_PATTERN);
    }
}
