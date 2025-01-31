package com.example.team_12_be.security.exception;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2UserNotFoundException extends OAuth2AuthenticationException {

    private final String email;

    public OAuth2UserNotFoundException(String email) {
        super("User not found");
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
