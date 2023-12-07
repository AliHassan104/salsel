package com.salsel.service.impl;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Department;
import com.salsel.model.DepartmentCategory;
import com.salsel.repository.DepartmentCategoryRepository;
import com.salsel.repository.DepartmentRepository;
import com.salsel.service.DepartmentCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentCategoryServiceImpl implements DepartmentCategoryService {

    private final DepartmentCategoryRepository departmentCategoryRepository;
    private final DepartmentRepository departmentRepository;

    public DepartmentCategoryServiceImpl(DepartmentCategoryRepository departmentCategoryRepository, DepartmentRepository departmentRepository) {
        this.departmentCategoryRepository = departmentCategoryRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public DepartmentCategoryDto save(DepartmentCategoryDto departmentCategoryDto) {
        DepartmentCategory departmentCategory = toEntity(departmentCategoryDto);
        departmentCategory.setStatus(true);

        Department department = departmentRepository.findById(departmentCategory.getDepartment().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %d", departmentCategory.getDepartment().getId())));

        departmentCategory.setDepartment(department);
        DepartmentCategory createdDepartmentCategory = departmentCategoryRepository.save(departmentCategory);
        return toDto(createdDepartmentCategory);
    }

    @Override
    public List<DepartmentCategoryDto> getAll(Boolean status) {
        List<DepartmentCategory> departmentCategoryList = departmentCategoryRepository.findAllInDesOrderByIdAndStatus(status);
        List<DepartmentCategoryDto> departmentCategoryDtoList = new ArrayList<>();

        for (DepartmentCategory departmentCategory : departmentCategoryList) {
            DepartmentCategoryDto departmentCategoryDto = toDto(departmentCategory);
            departmentCategoryDtoList.add(departmentCategoryDto);
        }
        return departmentCategoryDtoList;
    }

    @Override
    public PaginationResponse getAllPaginatedDepartmentCategory(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<DepartmentCategory> pageDepartmentCategory = departmentCategoryRepository.findAllInDesOrderByIdAndStatus(page);
        List<DepartmentCategory> departmentCategoryList = pageDepartmentCategory.getContent();

        List<DepartmentCategoryDto> departmentCategoryDtoList = new ArrayList<>();
        for (DepartmentCategory departmentCategory : departmentCategoryList) {
            DepartmentCategoryDto departmentCategoryDto = toDto(departmentCategory);
            departmentCategoryDtoList.add(departmentCategoryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(departmentCategoryDtoList);
        paginationResponse.setPageNumber(pageDepartmentCategory.getNumber());
        paginationResponse.setPageSize(pageDepartmentCategory.getSize());
        paginationResponse.setTotalElements(pageDepartmentCategory.getNumberOfElements());
        paginationResponse.setTotalPages(pageDepartmentCategory.getTotalPages());
        paginationResponse.setLastPage(pageDepartmentCategory.isLast());

        return paginationResponse;
    }

    @Override
    public PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<DepartmentCategory> pageDepartmentCategory = departmentCategoryRepository.findDepartmentCategoryByName(name,page);
        List<DepartmentCategory> departmentCategoryList = pageDepartmentCategory.getContent();

        List<DepartmentCategoryDto> departmentCategoryDtoList = new ArrayList<>();
        for (DepartmentCategory departmentCategory : departmentCategoryList) {
            DepartmentCategoryDto departmentCategoryDto = toDto(departmentCategory);
            departmentCategoryDtoList.add(departmentCategoryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(departmentCategoryDtoList);
        paginationResponse.setPageNumber(pageDepartmentCategory.getNumber());
        paginationResponse.setPageSize(pageDepartmentCategory.getSize());
        paginationResponse.setTotalElements(pageDepartmentCategory.getNumberOfElements());
        paginationResponse.setTotalPages(pageDepartmentCategory.getTotalPages());
        paginationResponse.setLastPage(pageDepartmentCategory.isLast());

        return paginationResponse;
    }

    @Override
    public DepartmentCategoryDto findById(Long id) {
        DepartmentCategory departmentCategory = departmentCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("DepartmentCategory not found for id => %d", id)));
        return toDto(departmentCategory);
    }

    @Override
    public DepartmentCategoryDto findByName(String name) {
        DepartmentCategory departmentCategory = departmentCategoryRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("DepartmentCategory not found for name => %s", name)));
        return toDto(departmentCategory);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        DepartmentCategory departmentCategory = departmentCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("DepartmentCategory not found for id => %d", id)));
        departmentCategoryRepository.setStatusInactive(departmentCategory.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        DepartmentCategory departmentCategory = departmentCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("DepartmentCategory not found for id => %d", id)));
        departmentCategoryRepository.setStatusActive(departmentCategory.getId());
    }

    @Override
    @Transactional
    public DepartmentCategoryDto update(Long id, DepartmentCategoryDto departmentCategoryDto) {
        DepartmentCategory existingDepartmentCategory = departmentCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("DepartmentCategory not found for id => %d", id)));

        existingDepartmentCategory.setName(departmentCategoryDto.getName());

        existingDepartmentCategory.setDepartment(departmentRepository.findById(departmentCategoryDto.getDepartment().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %d", departmentCategoryDto.getDepartment().getId()))));

        DepartmentCategory updatedDepartmentCategory = departmentCategoryRepository.save(existingDepartmentCategory);
        return toDto(updatedDepartmentCategory);
    }

    public DepartmentCategoryDto toDto(DepartmentCategory departmentCategory) {
        return DepartmentCategoryDto.builder()
                .id(departmentCategory.getId())
                .name(departmentCategory.getName())
                .status(departmentCategory.getStatus())
                .department(departmentCategory.getDepartment())
                .build();
    }

    public DepartmentCategory toEntity(DepartmentCategoryDto departmentCategoryDto) {
        return DepartmentCategory.builder()
                .id(departmentCategoryDto.getId())
                .name(departmentCategoryDto.getName())
                .status(departmentCategoryDto.getStatus())
                .department(departmentCategoryDto.getDepartment())
                .build();
    }
}
