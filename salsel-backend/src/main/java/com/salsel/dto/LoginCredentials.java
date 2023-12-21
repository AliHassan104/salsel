package com.salsel.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginCredentials {
    private String email;
    private String password;
}
