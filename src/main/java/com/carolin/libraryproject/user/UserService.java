package com.carolin.libraryproject.user;

import com.carolin.libraryproject.exceptionHandler.EmailAlreadyExcistException;
import com.carolin.libraryproject.exceptionHandler.NotValidEmailException;
import com.carolin.libraryproject.exceptionHandler.NotValidPasswordException;
import com.carolin.libraryproject.exceptionHandler.UserNotFoundException;
import com.carolin.libraryproject.validation.EmailValidator;
import com.carolin.libraryproject.validation.PasswordValidator;
import com.carolin.libraryproject.user.userDto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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

        if (!PasswordValidator.isPasswordValid(user.getPassword())) {
            throw new NotValidPasswordException("Password is not valid: must be at least 8 characters long, " +
                    "one uppercase letter, and one digit");
        }

        if (!EmailValidator.isEmailValid(user.getEmail())) {
            throw new NotValidEmailException("Email is not valid");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExcistException("Email is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }



}
