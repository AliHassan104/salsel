package com.salsel.service.impl;

import com.salsel.dto.DepartmentDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Department;
import com.salsel.repository.DepartmentRepository;
import com.salsel.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public DepartmentDto save(DepartmentDto departmentDto) {
        Department department = toEntity(departmentDto);
        department.setStatus(true);
        Department createdDepartment = departmentRepository.save(department);
        return toDto(createdDepartment);
    }

    @Override
    public List<DepartmentDto> getAll() {
        List<Department> departmentList = departmentRepository.findAllInDesOrderByIdAndStatus();
        List<DepartmentDto> departmentDtoList = new ArrayList<>();

        for (Department department : departmentList) {
            DepartmentDto departmentDto = toDto(department);
            departmentDtoList.add(departmentDto);
        }
        return departmentDtoList;
    }

    @Override
    public PaginationResponse getAllPaginatedDepartment(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Department> pageDepartment = departmentRepository.findAllInDesOrderByIdAndStatus(page);
        List<Department> departmentList = pageDepartment.getContent();

        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        for (Department department : departmentList) {
            DepartmentDto departmentDto = toDto(department);
            departmentDtoList.add(departmentDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(departmentDtoList);
        paginationResponse.setPageNumber(pageDepartment.getNumber());
        paginationResponse.setPageSize(pageDepartment.getSize());
        paginationResponse.setTotalElements(pageDepartment.getNumberOfElements());
        paginationResponse.setTotalPages(pageDepartment.getTotalPages());
        paginationResponse.setLastPage(pageDepartment.isLast());

        return paginationResponse;
    }

    @Override
    public PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Department> pageDepartment = departmentRepository.findDepartmentsByName(name,page);
        List<Department> departmentList = pageDepartment.getContent();

        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        for (Department department : departmentList) {
            DepartmentDto departmentDto = toDto(department);
            departmentDtoList.add(departmentDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(departmentDtoList);
        paginationResponse.setPageNumber(pageDepartment.getNumber());
        paginationResponse.setPageSize(pageDepartment.getSize());
        paginationResponse.setTotalElements(pageDepartment.getNumberOfElements());
        paginationResponse.setTotalPages(pageDepartment.getTotalPages());
        paginationResponse.setLastPage(pageDepartment.isLast());

        return paginationResponse;
    }

    @Override
    public DepartmentDto findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %d", id)));
        return toDto(department);
    }

    @Override
    public DepartmentDto findByName(String name) {
        Department department = departmentRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for name => %s", name)));
        return toDto(department);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %d", id)));
        departmentRepository.setStatusInactive(department.getId());
    }

    @Override
    @Transactional
    public DepartmentDto update(Long id, DepartmentDto departmentDto) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %d", id)));

        existingDepartment.setName(departmentDto.getName());

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return toDto(updatedDepartment);
    }

    public DepartmentDto toDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .status(department.getStatus())
                .build();
    }

    public Department toEntity(DepartmentDto departmentDto) {
        return Department.builder()
                .id(departmentDto.getId())
                .name(departmentDto.getName())
                .status(departmentDto.getStatus())
                .build();
    }

}
