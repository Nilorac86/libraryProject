package com.carolin.libraryproject.event;

import com.carolin.libraryproject.event.eventDto.UserRegistrationEventDto;
import com.carolin.libraryproject.user.User;

import java.time.LocalDateTime;

public class UserEventMapper {

    public static UserRegistrationEventDto toUserRegistrationEventDto(User user) {

        return
    new UserRegistrationEventDto(
            user.getEmail(),
            user.getRole(),
            user.getRegistrationDate()
    );
    }
}
