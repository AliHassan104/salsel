package com.salsel.dto;

import com.salsel.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private LocalDate createdAt;
    private Long employeeId;
    private String name;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private Boolean status;
    private String city;
    private String country;
    private Long employeeReferenceId;
    private Set<Role> roles = new HashSet<>();
}
