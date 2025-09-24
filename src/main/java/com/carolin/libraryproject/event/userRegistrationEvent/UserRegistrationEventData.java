package com.carolin.libraryproject.event.userRegistrationEvent;

import java.time.LocalDateTime;

public class UserRegistrationEventData {
    private final String email;
    private final String role;
    private final LocalDateTime registeredAt;

    public UserRegistrationEventData(String email, String role, LocalDateTime registeredAt) {
        this.email = email;
        this.role = role;
        this.registeredAt = registeredAt;

    }

    public String getEmail() {

        return email;
    }

    public String getRole() {

        return role;
    }

    public LocalDateTime getRegisteredAt() {

        return registeredAt;
    }

}
