package com.salsel.controller;


import com.salsel.dto.DepartmentDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/department")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(departmentService.save(departmentDto));
    }

    @GetMapping("/department")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<DepartmentDto>> getAllDepartment() {
        List<DepartmentDto> departmentDtoList = departmentService.getAll();
        return ResponseEntity.ok(departmentDtoList);
    }

    @GetMapping("/department/page")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PaginationResponse> getAllPaginatedDepartment(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {
        PaginationResponse paginationResponse = departmentService.getAllPaginatedDepartment(pageNumber, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/department/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
        DepartmentDto departmentDto = departmentService.findById(id);
        return ResponseEntity.ok(departmentDto);
    }

    @GetMapping("/department/name/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DepartmentDto> getDepartmentByName(@PathVariable String name) {
        DepartmentDto departmentDto = departmentService.findByName(name);
        return ResponseEntity.ok(departmentDto);
    }

    @DeleteMapping("/department/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/department/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id,@RequestBody DepartmentDto departmentDto) {
        DepartmentDto updatedDepartmentDto = departmentService.update(id, departmentDto);
        return ResponseEntity.ok(updatedDepartmentDto);
    }

}
