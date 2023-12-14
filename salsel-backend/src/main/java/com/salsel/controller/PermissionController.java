package com.salsel.controller;

import com.salsel.dto.PermissionDto;
import com.salsel.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/permission")
    @PreAuthorize("hasAuthority('READ_PERMISSION')")
    public ResponseEntity<List<PermissionDto>> getAllPermission() {
        List<PermissionDto> permissionDtoList = permissionService.getAll();
        return ResponseEntity.ok(permissionDtoList);
    }

    @GetMapping("/permission/{id}")
    @PreAuthorize("hasAuthority('READ_PERMISSION')")
    public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long id) {
        PermissionDto permissionDto = permissionService.findById(id);
        return ResponseEntity.ok(permissionDto);
    }
}
