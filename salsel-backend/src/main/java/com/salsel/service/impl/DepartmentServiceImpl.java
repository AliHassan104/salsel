package com.salsel.service.impl;

import com.salsel.dto.DepartmentDto;
import com.salsel.dto.TicketDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Department;
import com.salsel.model.DepartmentCategory;
import com.salsel.model.Ticket;
import com.salsel.repository.DepartmentRepository;
import com.salsel.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<DepartmentDto> findAll() {
        return toDtoList(departmentRepository.findAll());
    }

    @Override
    public DepartmentDto findById(Long id) {
       return toDto(departmentRepository.findById(id).get());
    }

    @Override
    public DepartmentDto save(DepartmentDto departmentDto) {
       return toDto(departmentRepository.save(toEntity(departmentDto)));
    }

    @Override
    public DepartmentDto update(DepartmentDto departmentDto, Long id) throws Exception {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %d", id)));

        department.setName(departmentDto.getName());

        Department updatedDepartment = departmentRepository.save(department);
        return toDto(updatedDepartment);
    }

    @Override
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentDto> findByPage(Integer pageNumber, Integer pageSize) {
        return toDtoList(departmentRepository.findAll());
    }


    public List<DepartmentDto> toDtoList(List<Department> departments){
        List<DepartmentDto> departmentDtos = new ArrayList<>();
        for (Department department : departments) {
            DepartmentDto ticketDto = toDto(department);
            departmentDtos.add(ticketDto);
        }
        return departmentDtos;
    }

    public DepartmentDto toDto(Department department) {
        return DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())

                .build();
    }

    public Department toEntity(DepartmentDto departmentDto) {
        return Department.builder()
                .id(departmentDto.getId())
                .name(departmentDto.getName())

                .build();
    }

}
