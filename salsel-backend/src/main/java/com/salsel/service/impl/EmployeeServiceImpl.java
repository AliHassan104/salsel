package com.salsel.service.impl;

import com.salsel.dto.EmployeeDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.*;
import com.salsel.repository.EmployeeRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.EmployeeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public EmployeeDto save(EmployeeDto employeeDto) {
        Employee employee = toEntity(employeeDto);
        employee.setStatus(true);

        User user = userRepository.findById(employee.getUserId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", employee.getUserId())));

        employee.setUserId(user.getId());
        Employee createdEmployee = employeeRepository.save(employee);
        return toDto(createdEmployee);
    }

    @Override
    public List<EmployeeDto> getAll(Boolean status) {
        List<Employee> employeeList = employeeRepository.findAllInDesOrderByIdAndStatus(status);
        return employeeList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Employee not found for id => %d", id)));
        return toDto(employee);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Employee not found for id => %d", id)));
        employeeRepository.setStatusInactive(employee.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Employee not found for id => %d", id)));
        employeeRepository.setStatusActive(employee.getId());
    }

    @Override
    @Transactional
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Employee not found for id => %d", id)));

        existingEmployee.setNationality(employeeDto.getNationality());
        existingEmployee.setJobTitle(employeeDto.getJobTitle());
        existingEmployee.setDepartment(employeeDto.getDepartment());
        existingEmployee.setSalary(employeeDto.getSalary());
        existingEmployee.setHousing(employeeDto.getHousing());
        existingEmployee.setTransportation(employeeDto.getTransportation());
        existingEmployee.setOtherAllowance(employeeDto.getOtherAllowance());

        User user = userRepository.findById(employeeDto.getUserId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", employeeDto.getUserId())));

        employeeDto.setUserId(user.getId());
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return toDto(updatedEmployee);
    }

    public EmployeeDto toDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .nationality(employee.getNationality())
                .jobTitle(employee.getJobTitle())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .housing(employee.getHousing())
                .transportation(employee.getTransportation())
                .otherAllowance(employee.getOtherAllowance())
                .userId(employee.getUserId())
                .status(employee.getStatus())
                .build();
    }

    public Employee toEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .nationality(employeeDto.getNationality())
                .jobTitle(employeeDto.getJobTitle())
                .department(employeeDto.getDepartment())
                .salary(employeeDto.getSalary())
                .housing(employeeDto.getHousing())
                .transportation(employeeDto.getTransportation())
                .otherAllowance(employeeDto.getOtherAllowance())
                .userId(employeeDto.getUserId())
                .status(employeeDto.getStatus())
                .build();
    }
}
