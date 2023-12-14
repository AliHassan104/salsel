package com.salsel.controller;

import com.salsel.dto.CityDto;
import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.service.DepartmentCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DepartmentCategoryController {

    private final DepartmentCategoryService departmentCategoryService;

    public DepartmentCategoryController(DepartmentCategoryService departmentCategoryService) {
        this.departmentCategoryService = departmentCategoryService;
    }

    @PostMapping("/department-category")
    @PreAuthorize("hasAuthority('CREATE_DEPARTMENT_CATEGORY')")
    public ResponseEntity<DepartmentCategoryDto> createDepartmentCategory(@RequestBody DepartmentCategoryDto departmentCategoryDto) {
        return ResponseEntity.ok(departmentCategoryService.save(departmentCategoryDto));
    }

    @GetMapping("/department-category")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT_CATEGORY')")
    public ResponseEntity<List<DepartmentCategoryDto>> getAllDepartmentCategory() {
        List<DepartmentCategoryDto> departmentCategoryDtoList = departmentCategoryService.getAll();
        return ResponseEntity.ok(departmentCategoryDtoList);
    }

    @GetMapping("/department-category/department/{id}")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT_CATEGORY')")
    public ResponseEntity<List<DepartmentCategoryDto>> getAllDepartmentCategoriesByDepartment(@PathVariable Long id) {
        List<DepartmentCategoryDto> departmentCategoryDtoList = departmentCategoryService.getAllByDepartment(id);
        return ResponseEntity.ok(departmentCategoryDtoList);
    }

    @GetMapping("/department-category/page")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT_CATEGORY')")
    public ResponseEntity<PaginationResponse> getAllPaginatedDepartment(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {
        PaginationResponse paginationResponse = departmentCategoryService.getAllPaginatedDepartmentCategory(pageNumber, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/department-category/{id}")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT_CATEGORY')")
    public ResponseEntity<DepartmentCategoryDto> getDepartmentCategoryById(@PathVariable Long id) {
        DepartmentCategoryDto departmentCategoryDto = departmentCategoryService.findById(id);
        return ResponseEntity.ok(departmentCategoryDto);
    }

    @GetMapping("/department-category/name/{name}")
    @PreAuthorize("hasAuthority('READ_DEPARTMENT_CATEGORY')")
    public ResponseEntity<DepartmentCategoryDto> getDepartmentCategoryByName(@PathVariable String name) {
        DepartmentCategoryDto departmentCategoryDto = departmentCategoryService.findByName(name);
        return ResponseEntity.ok(departmentCategoryDto);
    }

    @DeleteMapping("/department-category/{id}")
    @PreAuthorize("hasAuthority('DELETE_DEPARTMENT_CATEGORY')")
    public ResponseEntity<Void> deleteDepartmentCategory(@PathVariable Long id) {
        departmentCategoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/department-category/{id}")
    @PreAuthorize("hasAuthority('CREATE_DEPARTMENT_CATEGORY')")
    public ResponseEntity<DepartmentCategoryDto> updateDepartmentCategory(@PathVariable Long id,@RequestBody DepartmentCategoryDto departmentCategoryDto) {
        DepartmentCategoryDto updatedDepartmentCategoryDto = departmentCategoryService.update(id, departmentCategoryDto);
        return ResponseEntity.ok(updatedDepartmentCategoryDto);
    }
}
