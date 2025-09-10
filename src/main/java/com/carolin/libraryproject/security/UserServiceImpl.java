package com.carolin.libraryproject.security;

import com.carolin.libraryproject.exceptionHandler.UserNotFoundException;
import com.carolin.libraryproject.user.User;
import com.carolin.libraryproject.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new CustomUserDetails(user);

         //Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRole());


       // return org.springframework.security.core.userdetails.User.builder()
//                .username(user.getEmail())
//                .password(user.getPassword())
//                .authorities(user.getRole())
//                .disabled(!user.isEnabled())
//                .build();


    }

}
