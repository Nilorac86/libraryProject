package com.carolin.libraryproject.event.userRegistrationEvent;

import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {

    private final UserRegistrationEventData userData;
    private final String ipAddress;


    // Innehåller information om den registerade användaren och sparar ipadress där kontot skapades
    public UserRegistrationEvent(Object source, UserRegistrationEventData userData, String ipAddress) {
        super(source);
        this.userData = userData;
        this.ipAddress = ipAddress;
    }

    public UserRegistrationEventData getUserData() {

        return userData;
    }


    public String getIpAddress() {
        return ipAddress;
    }
}
