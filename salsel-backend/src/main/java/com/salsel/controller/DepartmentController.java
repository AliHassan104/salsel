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
    @PreAuthorize("hasAuthority('CREATE_DEPARTMENT') and hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(departmentService.save(departmentDto));
    }

    @GetMapping("/department")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<List<DepartmentDto>> getAllDepartment(@RequestParam(value = "status") Boolean status) {
        List<DepartmentDto> departmentDtoList = departmentService.getAll(status);
        return ResponseEntity.ok(departmentDtoList);
    }

    @GetMapping("/department/page")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<PaginationResponse> getAllPaginatedDepartment(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {
        PaginationResponse paginationResponse = departmentService.getAllPaginatedDepartment(pageNumber, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/department/{id}")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
        DepartmentDto departmentDto = departmentService.findById(id);
        return ResponseEntity.ok(departmentDto);
    }

    @GetMapping("/department/name/{name}")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<DepartmentDto> getDepartmentByName(@PathVariable String name) {
        DepartmentDto departmentDto = departmentService.findByName(name);
        return ResponseEntity.ok(departmentDto);
    }

    @DeleteMapping("/department/{id}")
    @PreAuthorize("hasAuthority('DELETE_DEPARTMENT') and hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/department/{id}")
    @PreAuthorize("hasAuthority('CREATE_DEPARTMENT') and hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id,@RequestBody DepartmentDto departmentDto) {
        DepartmentDto updatedDepartmentDto = departmentService.update(id, departmentDto);
        return ResponseEntity.ok(updatedDepartmentDto);
    }

    @PutMapping("/department/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_DEPARTMENT') and hasAuthority('READ_DEPARTMENT')")
    public ResponseEntity<Void> updateDepartmentStatusToActive(@PathVariable Long id) {
        departmentService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

}
