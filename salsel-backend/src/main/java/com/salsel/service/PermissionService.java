package com.salsel.service;

import com.salsel.dto.PermissionDto;

import java.util.List;

public interface PermissionService {
    List<PermissionDto> getAll();
    PermissionDto findById(Long id);
}
