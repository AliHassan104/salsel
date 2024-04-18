package com.salsel.service.impl;

import com.salsel.dto.EmployeeDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.*;
import com.salsel.repository.EmployeeAttachmentRepository;
import com.salsel.repository.EmployeeRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.BucketService;
import com.salsel.service.EmployeeService;
import com.salsel.utils.HelperUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.salsel.service.impl.bucketServiceImpl.EMPLOYEE;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final EmployeeAttachmentRepository employeeAttachmentRepository;
    private final BucketService bucketService;
    private final HelperUtils helperUtils;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserRepository userRepository, EmployeeAttachmentRepository employeeAttachmentRepository, BucketService bucketService, HelperUtils helperUtils) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.employeeAttachmentRepository = employeeAttachmentRepository;
        this.bucketService = bucketService;
        this.helperUtils = helperUtils;
    }

//    @Override
//    @Transactional
//    public EmployeeDto save(EmployeeDto employeeDto, MultipartFile passport, MultipartFile id, List<MultipartFile> docs) {
//        Employee employee = toEntity(employeeDto);
//        employee.setStatus(true);
//
//        User user = userRepository.findById(employee.getUserId())
//                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", employee.getUserId())));
//
//        employee.setUserId(user.getId());
//        Employee createdEmployee = employeeRepository.save(employee);
//
//        if(passport != null || !passport.isEmpty() || id != null || !id.isEmpty() || docs != null && !docs.isEmpty()){
//            String folderName = "Employee_" + createdEmployee.getId();
//
//            String filename = passport.getOriginalFilename();
//            String fileExtension = "." + FilenameUtils.getExtension(passport.getOriginalFilename());
//            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
//            String newFileName = FilenameUtils.getBaseName(filename) + "_" + timestamp + fileExtension;
//
//            String url = bucketService.save(passport, folderName, newFileName, EMPLOYEE);
//
//            String filename = id.getOriginalFilename();
//            String fileExtension = "." + FilenameUtils.getExtension(passport.getOriginalFilename());
//            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
//            String newFileName = FilenameUtils.getBaseName(filename) + "_" + timestamp + fileExtension;
//
//            String url = bucketService.save(id, folderName, newFileName, EMPLOYEE);
//
//            // Save docs to S3 bucket if provided
//            if (docs != null && !docs.isEmpty()) {
//                List<String> savedDocsUrls = helperUtils.savePdfListToS3(docs, folderName, EMPLOYEE);
//
//                List<EmployeeAttachment> employeeAttachmentList = new ArrayList<>();
//                for (String savedDocUrl : savedDocsUrls) {
//                    EmployeeAttachment employeeAttachment = new EmployeeAttachment();
//                    employeeAttachment.setFilePath(savedDocUrl);
//                    employeeAttachment.setEmployee(createdEmployee);
//                    employeeAttachmentList.add(employeeAttachment);
//                }
//
//                createdEmployee.setAttachments(employeeAttachmentList);
//                employeeAttachmentRepository.saveAll(employeeAttachmentList);
//                logger.info("Docs are uploaded to S3 in folder '{}'.", folderName);
//            }
//        }
//
//        return toDto(createdEmployee);
//    }

    @Override
    @Transactional
    public EmployeeDto save(EmployeeDto employeeDto, MultipartFile passport, MultipartFile id, List<MultipartFile> docs) {
        Employee employee = toEntity(employeeDto);
        employee.setStatus(true);

        User user = userRepository.findById(employee.getUserId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", employee.getUserId())));

        employee.setUserId(user.getId());
        Employee createdEmployee = employeeRepository.save(employee);

        helperUtils.saveFileToBucketAndSetUrl(passport, createdEmployee, "passport");
        helperUtils.saveFileToBucketAndSetUrl(id, createdEmployee, "id");

        if (docs != null && !docs.isEmpty()) {
            String folderName = "Employee_" + createdEmployee.getId();
            List<String> savedDocsUrls = helperUtils.savePdfListToS3(docs, folderName, EMPLOYEE);

            List<EmployeeAttachment> employeeAttachmentList = new ArrayList<>();
            for (String savedDocUrl : savedDocsUrls) {
                EmployeeAttachment employeeAttachment = new EmployeeAttachment();
                employeeAttachment.setFilePath(savedDocUrl);
                employeeAttachment.setEmployee(createdEmployee);
                employeeAttachmentList.add(employeeAttachment);
            }

            createdEmployee.setAttachments(employeeAttachmentList);
            employeeAttachmentRepository.saveAll(employeeAttachmentList);
            logger.info("Docs are uploaded to S3 in folder '{}'.", folderName);
        }

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
        existingEmployee.setMobile(employeeDto.getMobile());
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
                .mobile(employee.getMobile())
                .transportation(employee.getTransportation())
                .otherAllowance(employee.getOtherAllowance())
                .passportFilePath(employee.getPassportFilePath())
                .idFilePath(employee.getIdFilePath())
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
                .mobile(employeeDto.getMobile())
                .transportation(employeeDto.getTransportation())
                .otherAllowance(employeeDto.getOtherAllowance())
                .passportFilePath(employeeDto.getPassportFilePath())
                .idFilePath(employeeDto.getIdFilePath())
                .userId(employeeDto.getUserId())
                .status(employeeDto.getStatus())
                .build();
    }
}
