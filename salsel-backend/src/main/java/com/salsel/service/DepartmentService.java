package com.salsel.service;

import com.salsel.dto.DepartmentDto;
import com.salsel.dto.TicketDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {

    List<DepartmentDto> findAll();
    DepartmentDto findById(Long id);
    DepartmentDto save(DepartmentDto departmentDto);
    void delete(Long id);
    DepartmentDto update(DepartmentDto departmentDto , Long id) throws Exception;
    Page<DepartmentDto> findByPage(Integer pageNumber, Integer pageSize);

}
