package com.salsel.service;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.TicketDto;

import java.util.List;

public interface DepartmentCategoryService {

    List<DepartmentCategoryDto> findAll();
    DepartmentCategoryDto findById(Long id);
    DepartmentCategoryDto save(DepartmentCategoryDto departmentCategoryDto);
    void delete(Long id);
    DepartmentCategoryDto update(DepartmentCategoryDto departmentCategoryDto , Long id) throws Exception;
    List<DepartmentCategoryDto> findByPage(Integer pageNumber, Integer pageSize);

}
