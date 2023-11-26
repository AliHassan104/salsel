package com.salsel.service.impl;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Department;
import com.salsel.model.DepartmentCategory;
import com.salsel.model.ProductType;
import com.salsel.model.Ticket;
import com.salsel.repository.DepartmentCategoryRepository;
import com.salsel.repository.DepartmentRepository;
import com.salsel.service.DepartmentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentCategoryServiceImpl implements DepartmentCategoryService {

    @Autowired
    private DepartmentCategoryRepository departmentCategoryRepository;


    @Override
    public List<DepartmentCategoryDto> findAll() {
        return toDtoList(departmentCategoryRepository.findAll());
    }

    @Override
    public DepartmentCategoryDto findById(Long id) {
        return toDto(departmentCategoryRepository.findById(id).get());
    }

    @Override
    public DepartmentCategoryDto save(DepartmentCategoryDto departmentCategoryDto) {
        return toDto(departmentCategoryRepository.save(toEntity(departmentCategoryDto)));
    }

    @Override
    public void delete(Long id) {
        departmentCategoryRepository.deleteById(id);
    }

    @Override
    public DepartmentCategoryDto update(DepartmentCategoryDto departmentCategoryDto, Long id) throws Exception {

        DepartmentCategory departmentCategory = departmentCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("DepartmentCategory not found for id => %d", id)));

        departmentCategory.setName(departmentCategoryDto.getName());
        departmentCategory.setCode(departmentCategoryDto.getCode());
        departmentCategory.setDepartment(departmentCategoryDto.getDepartment());

        DepartmentCategory updatedDepartmentCategory = departmentCategoryRepository.save(departmentCategory);
        return toDto(updatedDepartmentCategory);
    }

    @Override
    public List<DepartmentCategoryDto> findByPage(Integer pageNumber, Integer pageSize) {
        Page<DepartmentCategory> departmentCategories = departmentCategoryRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        return toDtoList(departmentCategories.toList());
    }

    public List<DepartmentCategoryDto> toDtoList(List<DepartmentCategory> departmentCategories){
        List<DepartmentCategoryDto> departmentCategoriesDtos = new ArrayList<>();
        for (DepartmentCategory departmentCategory : departmentCategories) {
            DepartmentCategoryDto ticketDto = toDto(departmentCategory);
            departmentCategoriesDtos.add(ticketDto);
        }
        return departmentCategoriesDtos;
    }

    public DepartmentCategoryDto toDto(DepartmentCategory departmentCategory) {
        return DepartmentCategoryDto.builder()
                .id(departmentCategory.getId())
                .name(departmentCategory.getName())
                .code(departmentCategory.getCode())
                .department(departmentCategory.getDepartment())

                .build();
    }

    public DepartmentCategory toEntity(DepartmentCategoryDto departmentCategoryDto) {
        return DepartmentCategory.builder()
                .id(departmentCategoryDto.getId())
                .name(departmentCategoryDto.getName())
                .code(departmentCategoryDto.getCode())
                .department(departmentCategoryDto.getDepartment())

                .build();
    }

}
