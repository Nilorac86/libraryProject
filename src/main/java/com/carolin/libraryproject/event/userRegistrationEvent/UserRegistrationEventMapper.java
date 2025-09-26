package com.carolin.libraryproject.event.userRegistrationEvent;

import com.carolin.libraryproject.user.User;

public class UserRegistrationEventMapper {

    // Samla data på ett ställe
    public static UserRegistrationEventData toUserRegistrationData(User user) {

        return
    new UserRegistrationEventData(
            user.getEmail(),
            user.getRole(),
            user.getRegistrationDate()
    );
    }
}
