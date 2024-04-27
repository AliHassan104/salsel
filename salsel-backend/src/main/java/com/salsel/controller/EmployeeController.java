package com.salsel.controller;

import com.salsel.dto.EmployeeDto;
import com.salsel.service.EmployeeService;
import com.salsel.service.PdfGenerationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final PdfGenerationService pdfGenerationService;
    public EmployeeController(EmployeeService employeeService, PdfGenerationService pdfGenerationService) {
        this.employeeService = employeeService;
        this.pdfGenerationService = pdfGenerationService;
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
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id,
                                                      @RequestPart(name = "employeeDto") EmployeeDto employeeDto,
                                                      @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                                      @RequestParam("fileNames") List<String> fileNames,
                                                      @RequestPart(name = "passport", required = false) MultipartFile passportFile,
                                                      @RequestPart(name = "idFile", required = false) MultipartFile idFile) {
        EmployeeDto updatedEmployeeDto = employeeService.update(id, employeeDto, files, fileNames, passportFile, idFile);
        return ResponseEntity.ok(updatedEmployeeDto);
    }

    @PutMapping("/employee/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE') and hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<Void> updateEmployeeStatusToActive(@PathVariable Long id) {
        employeeService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/employee/pdf/{id}")
    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE') and hasAuthority('READ_EMPLOYEE')")
    public ResponseEntity<byte[]> generateEmployeePdf(@PathVariable Long id) {
        // Generate PDF
        byte[] pdfBytes = pdfGenerationService.generateEmployeePdf(id);

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "EmployeeDetails.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}

