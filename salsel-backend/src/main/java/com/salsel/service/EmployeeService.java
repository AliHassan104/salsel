package com.salsel.service;

import com.salsel.dto.EmployeeDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    EmployeeDto save(EmployeeDto employeeDto, MultipartFile passport, MultipartFile id, List<MultipartFile> docs);
    List<EmployeeDto> getAll(Boolean status);
    EmployeeDto findById(Long id);
    void deleteById(Long id);
    void setToActiveById(Long id);
    EmployeeDto update(Long id, EmployeeDto employeeDto);
}
