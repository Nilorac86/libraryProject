package com.carolin.libraryproject.security;

import com.carolin.libraryproject.authentication.LoginAttemptService;
import com.carolin.libraryproject.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;


public class CustomUserDetails implements UserDetails {

    private final User user;
    private final LoginAttemptService loginAttemptService;

    public CustomUserDetails(User user, LoginAttemptService loginAttemptService) {
        this.user = user;
        this.loginAttemptService = loginAttemptService;
    }


    // Returnerar användaren
    public User getUser() {
        return user;
    }

    // Roller/behörigheter för Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(user.getRole());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
       return !loginAttemptService.isBlocked(user.getEmail());
    }
    
}




