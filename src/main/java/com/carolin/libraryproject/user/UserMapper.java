package com.carolin.libraryproject.user;
import com.carolin.libraryproject.user.userDto.UserDto;
import com.carolin.libraryproject.user.userDto.UserRequestDto;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {


    // Mappar användare
    public UserDto toUserDto(User user) {

        if (user == null) {
            return null;
        }
        return new UserDto(

                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getRegistrationDate());

    }



    // Mappar användare med stream som returnerar en lista
    public List<UserDto> toDtoList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
        }

// Användare requestDto
    public User toUserEntity(UserRequestDto dto ) {
        if (dto == null) {
            return null;

        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;

    }
}


