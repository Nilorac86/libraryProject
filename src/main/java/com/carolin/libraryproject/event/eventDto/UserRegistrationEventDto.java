package com.carolin.libraryproject.event.eventDto;

import java.time.LocalDateTime;

public class UserRegistrationEventDto {
    private final String email;
    private final String role;
    private final LocalDateTime registeredAt;

    public UserRegistrationEventDto(String email, String role, LocalDateTime registeredAt) {
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
