package com.carolin.libraryproject.event;

import com.carolin.libraryproject.event.eventDto.UserRegistrationEventDto;
import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {

    private final UserRegistrationEventDto userData;


    public UserRegistrationEvent(Object source, UserRegistrationEventDto userData) {
        super(source);
        this.userData = userData;

    }

    public UserRegistrationEventDto getUserData() {
        return userData;
    }


}
