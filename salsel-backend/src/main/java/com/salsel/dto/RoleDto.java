package com.salsel.dto;

import com.salsel.model.Permission;
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
public class RoleDto {
    private Long id;
    private String name;
    private Set<Permission> permissions = new HashSet<>();
}
