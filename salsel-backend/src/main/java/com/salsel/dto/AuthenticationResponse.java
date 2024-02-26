package com.salsel.dto;

import lombok.Getter;

@Getter
public class AuthenticationResponse {
    private final String jwt;
    private final String email;

    public AuthenticationResponse(String jwt, String email) {
        this.jwt = jwt;
        this.email = email;
    }

    public String getJwt() {
        return jwt;
    }
}