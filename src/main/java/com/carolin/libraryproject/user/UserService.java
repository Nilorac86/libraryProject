package com.carolin.libraryproject.user;

import com.carolin.libraryproject.event.userRegistrationEvent.UserRegistrationEventMapper;
import com.carolin.libraryproject.event.userRegistrationEvent.UserRegistrationEvent;
import com.carolin.libraryproject.event.userRegistrationEvent.UserRegistrationEventData;
import com.carolin.libraryproject.exceptionHandler.EmailAlreadyExcistException;
import com.carolin.libraryproject.exceptionHandler.UserNotFoundException;
import com.carolin.libraryproject.loan.LoanRepository;
import com.carolin.libraryproject.user.userDto.UserDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoanRepository loanRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository, UserMapper userMapper, LoanRepository loanRepository, PasswordEncoder passwordEncoder,
                       ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.loanRepository = loanRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }


    // Lista alla användare i dto
    @PreAuthorize("hasRole ('ADMIN')")
    public List<UserDto> findAll() {

        return userMapper.toDtoList(userRepository.findAll());
    }


    // En optional som returnerar en mappad användare eller UserNotFoundException.
    @PreAuthorize("hasRole ('ADMIN')")
    public UserDto findUserByEmail(String email) {

       Optional<User> user = userRepository.findByEmail(email);

       return user.map(userMapper::toUserDto).orElseThrow(() -> new UserNotFoundException
               ("User not found"));
    }


    // Lägger till användare
    public User addUser(User user, String clientIp) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExcistException("Email is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        UserRegistrationEventData userEventDto = UserRegistrationEventMapper.toUserRegistrationData(savedUser);

        eventPublisher.publishEvent(new UserRegistrationEvent(this, userEventDto, clientIp));

        return savedUser;

    }


    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteUser(String email) throws IllegalAccessException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        boolean hasActiveLoans = loanRepository.existsByUserAndReturnedDateIsNull(user);

            if (hasActiveLoans) {
                throw new IllegalStateException("User have active loans and cannot be deleted");
            }

            userRepository.delete(user);
        }

    }
