package com.carolin.libraryproject.security;

import com.carolin.libraryproject.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;


public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
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
        return user.getEmail(); // vi använder email som username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // du kan anpassa detta om du vill
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // du kan anpassa detta om du vill
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // du kan anpassa detta om du vill
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // använd fältet i din User-entity
    }
    
}




