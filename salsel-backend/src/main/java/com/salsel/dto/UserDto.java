package com.salsel.dto;

import com.salsel.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String name;
    private String employeeId;
    private String email;
    private String password;
    private Boolean status;
    private Set<Role> roles = new HashSet<>();
}
