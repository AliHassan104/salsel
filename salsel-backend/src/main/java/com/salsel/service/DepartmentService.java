package com.salsel.service;

import com.salsel.dto.DepartmentDto;
import com.salsel.dto.PaginationResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentDto save(DepartmentDto departmentDto);
    List<DepartmentDto> getAll(Boolean status);
    PaginationResponse getAllPaginatedDepartment(Integer pageNumber, Integer pageSize);
    PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize);
    DepartmentDto findById(Long id);
    DepartmentDto findByName(String name);
    void deleteById(Long id);
    void setToActiveById(Long id);
    DepartmentDto update(Long id, DepartmentDto departmentDto);
}
