package com.carolin.libraryproject.security;

import com.carolin.libraryproject.authentication.LoginAttemptService;
import com.carolin.libraryproject.exceptionHandler.UserNotFoundException;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;


    public UserServiceImpl(UserRepository userRepository, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new CustomUserDetails(user, loginAttemptService);


    }

}
