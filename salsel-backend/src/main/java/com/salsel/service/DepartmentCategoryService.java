package com.salsel.service;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.PaginationResponse;

import java.util.List;

public interface DepartmentCategoryService {
    DepartmentCategoryDto save(DepartmentCategoryDto departmentCategoryDto);
    List<DepartmentCategoryDto> getAll();
    PaginationResponse getAllPaginatedDepartmentCategory(Integer pageNumber, Integer pageSize);
    PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize);
    DepartmentCategoryDto findById(Long id);
    DepartmentCategoryDto findByName(String name);
    void deleteById(Long id);
    DepartmentCategoryDto update(Long id, DepartmentCategoryDto departmentCategoryDto);
}
