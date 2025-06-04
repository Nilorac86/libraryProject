package com.carolin.libraryproject.user;

import com.carolin.libraryproject.exceptionHandler.UserNotFoundException;
import com.carolin.libraryproject.user.userDto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // Lista alla användare i dto
    public List<UserDto> findAll() {

        return userMapper.toDtoList(userRepository.findAll());
    }


    // En optional som returnerar en mappad användare eller UserNotFoundException.
    public UserDto findUserByEmail(String email) {

       Optional<User> user = userRepository.findByEmail(email);

       return user.map(userMapper::toUserDto).orElseThrow(() -> new UserNotFoundException
               ("User with email: " + email + " not found"));
    }


    // Lägger till användare
    public User addUser(User user) {

        return userRepository.save(user);
    }

}
