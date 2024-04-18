package com.salsel.controller;

import com.salsel.dto.DepartmentDto;
import com.salsel.dto.EmployeeDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.dto.TicketDto;
import com.salsel.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee")
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE') and hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<EmployeeDto> createEmployee(@RequestPart("employeeDto") EmployeeDto employeeDto,
                                                      @RequestPart(name = "passport", required = false) MultipartFile passport,
                                                      @RequestPart(name = "id", required = false) MultipartFile id,
                                                      @RequestPart(name = "docs", required = false) List<MultipartFile> docs) {
        return ResponseEntity.ok(employeeService.save(employeeDto, passport, id, docs));
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<List<EmployeeDto>> getAllEmployee(@RequestParam(value = "status") Boolean status) {
        List<EmployeeDto> employeeDtoList = employeeService.getAll(status);
        return ResponseEntity.ok(employeeDtoList);
    }

    @GetMapping("/employee/{id}")
    @PreAuthorize("hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        EmployeeDto employeeDto = employeeService.findById(id);
        return ResponseEntity.ok(employeeDto);
    }

    @DeleteMapping("/employee/{id}")
    @PreAuthorize("hasAuthority('DELETE_EMPLOYEE') and hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/employee/{id}")
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE') and hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,@RequestBody EmployeeDto employeeDto) {
        EmployeeDto updatedEmployeeDto = employeeService.update(id, employeeDto);
        return ResponseEntity.ok(updatedEmployeeDto);
    }

    @PutMapping("/employee/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE') and hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<Void> updateEmployeeStatusToActive(@PathVariable Long id) {
        employeeService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

}

