package com.carolin.libraryproject.user;
import com.carolin.libraryproject.user.userDto.UserDto;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {

        if (user == null) {
            return null;
        }
        return new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRegistrationDate());
    }

    public User toUserEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRegistrationDate(userDto.getRegistrationDate());
        return user;
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
        }
}
