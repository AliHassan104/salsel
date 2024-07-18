package com.salsel.service.impl;

import com.salsel.dto.EmployeeDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.*;
import com.salsel.repository.*;
import com.salsel.service.BucketService;
import com.salsel.service.EmployeeService;
import com.salsel.utils.EmailUtils;
import com.salsel.utils.HelperUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.salsel.service.impl.bucketServiceImpl.EMPLOYEE;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailUtils emailUtils;
    private final EmployeeAttachmentRepository employeeAttachmentRepository;
    private final BucketService bucketService;
    private final HelperUtils helperUtils;
    private final BCryptPasswordEncoder encoder;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserRepository userRepository, RoleRepository roleRepository, EmailUtils emailUtils, EmployeeAttachmentRepository employeeAttachmentRepository, BucketService bucketService, HelperUtils helperUtils, BCryptPasswordEncoder encoder) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailUtils = emailUtils;
        this.employeeAttachmentRepository = employeeAttachmentRepository;
        this.bucketService = bucketService;
        this.helperUtils = helperUtils;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public EmployeeDto save(EmployeeDto employeeDto, MultipartFile passport, MultipartFile id, List<MultipartFile> docs) {
        Employee employee = toEntity(employeeDto);
        employee.setCreatedAt(LocalDate.now());
        employee.setStatus(true);

        // Calculate total amount, replacing null values with 0
        double amount = Stream.of(
                        employee.getSalary(),
                        employee.getMobile(),
                        employee.getHousing(),
                        employee.getTransportation(),
                        employee.getOtherAllowance())
                .mapToDouble(value -> value != null ? value : 0.0)
                .sum();

        employee.setTotalAmount(amount);
        Employee latestEmployee = employeeRepository.findEmployeeByLatestId();

        if (latestEmployee != null) {
            String currentEmployeeNumber = latestEmployee.getEmployeeNumber();
            Long employeeNumber = Long.parseLong(currentEmployeeNumber);

            employeeNumber++;

            String newEmployeeNumber;
            if (currentEmployeeNumber.matches("^0\\d+$")) {
                newEmployeeNumber = String.format("%0" + currentEmployeeNumber.length() + "d", employeeNumber); // Maintain leading zeros
            } else {
                newEmployeeNumber = String.valueOf(employeeNumber); // No leading zeros
            }
            employee.setEmployeeNumber(newEmployeeNumber);
        } else {
            employee.setEmployeeNumber("0001");
        }

        Employee createdEmployee = employeeRepository.save(employee);

        if(employee.getCreateAsUser() != null) {
            Optional<User> existingUser = userRepository.findByEmail(employee.getEmail());
            if (!existingUser.isPresent()) {
                User user = new User();
                String password = helperUtils.generateResetPassword();
                user.setCreatedAt(LocalDate.now());
                user.setPassword(encoder.encode(password));
                user.setEmail(employee.getEmail());
                user.setName(employee.getName());
                user.setCreatedAt(LocalDate.now());
                user.setCountry(employee.getCountry());
                user.setCity(employee.getCity());
                user.setFirstname(employee.getFirstname());
                user.setLastname(employee.getLastname());
                user.setPhone(employee.getPhone());

                // Check if employeeId already exists, if yes, increment until unique
                Optional<User> userExist = userRepository.findByEmployeeId(employee.getEmployeeNumber());
                Long employeeId = Long.parseLong(employee.getEmployeeNumber());
                while (userExist.isPresent()) {
                    // If user with the same employeeId exists, increment employeeId by 1
                    employeeId++;
                    String incrementedEmployeeId = String.format("%0" + employee.getEmployeeNumber().length() + "d", employeeId);
                    userExist = userRepository.findByEmployeeId(incrementedEmployeeId);
                }
                user.setEmployeeId(String.format("%0" + employee.getEmployeeNumber().length() + "d", employeeId));


                Role role = roleRepository.findByName(employee.getPosition())
                        .orElseThrow(() -> new RecordNotFoundException("Role not found"));

                user.getRoles().add(role);
                user.setStatus(true);
                User createdUser = userRepository.save(user);
                emailUtils.sendWelcomeEmail(createdUser, password);
            }
        }


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
    public EmployeeDto update(Long id, EmployeeDto employeeDto, List<MultipartFile> files, List<String> fileNames, MultipartFile passportFile, MultipartFile idFile) {
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
        existingEmployee.setAddress(employeeDto.getAddress());
        existingEmployee.setFirstname(employeeDto.getFirstname());
        existingEmployee.setLastname(employeeDto.getLastname());
        existingEmployee.setName(employeeDto.getName());
        existingEmployee.setDateOfJoining(employeeDto.getDateOfJoining());
        existingEmployee.setNoOfAbsents(employeeDto.getNoOfAbsents());
        existingEmployee.setWorkingDays(employeeDto.getWorkingDays());

        if (passportFile != null && !passportFile.isEmpty()) {
            String folderKey = "Employee/Employee_" + id;
            bucketService.deleteFilesStartingWith(folderKey,"passport_");
            String folderName = "Employee_" + existingEmployee.getId();
            String savedUrl = helperUtils.savePdfToS3(passportFile, folderName, "passport");
            existingEmployee.setPassportFilePath(savedUrl);
            logger.info("Passport is uploaded to S3 in folder '{}'.", folderName);
        }

        if (idFile != null && !idFile.isEmpty()) {
            String folderKey = "Employee/Employee_" + id;
            bucketService.deleteFilesStartingWith(folderKey,"id_");
            String folderName = "Employee_" + existingEmployee.getId();
            String savedUrl = helperUtils.savePdfToS3(idFile, folderName, "id");
            existingEmployee.setIdFilePath(savedUrl);
            logger.info("Id is uploaded to S3 in folder '{}'.", folderName);
        }

        if (files != null && !files.isEmpty()) {
            // Delete existing files
            for (String fileName : fileNames) {
                bucketService.deleteFile(fileName);
            }

            // Save new files
            String folderName = "Employee_" + existingEmployee.getId();
            List<String> savedUrls = helperUtils.savePdfListToS3(files, folderName, "Employee");

            // Create new attachments
            List<EmployeeAttachment> employeeAttachmentList = new ArrayList<>();
            for (String savedUrl : savedUrls) {
                EmployeeAttachment employeeAttachment = new EmployeeAttachment();
                employeeAttachment.setFilePath(savedUrl);
                employeeAttachment.setEmployee(existingEmployee);
                employeeAttachmentList.add(employeeAttachment);
            }

            // Update existing Employee attachments
            existingEmployee.getAttachments().clear();
            existingEmployee.getAttachments().addAll(employeeAttachmentList);

            // Save attachments to the database
            employeeAttachmentRepository.saveAll(employeeAttachmentList);
            logger.info("Files are updated on S3 in folder '{}'.", folderName);
        }else {
            // No new files attached, delete existing files
            for (String fileName : fileNames) {
                bucketService.deleteFile(fileName);
            }

            // Clear existing attachments in the database
            existingEmployee.getAttachments().clear();
            logger.info("Existing Files are deleted.");
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return toDto(updatedEmployee);
    }

    @Override
    public List<Map<String, Object>> getAllEmployeeDataByExcel(String department,String country) {
        List<Employee> employeeList = employeeRepository.findEmployee(department,country);
        List<Map<String, Object>> result = new ArrayList<>();
        int count = 1;

        for (Employee employee : employeeList) {
            Map<String, Object> billingMap = new LinkedHashMap<>();
            billingMap.put("#", count++);
            billingMap.put("Name", employee.getName());
            billingMap.put("Phone", employee.getPhone());
            billingMap.put("Employee Number", employee.getEmployeeNumber());
            billingMap.put("Date of Joining", employee.getDateOfJoining());
            billingMap.put("Working Days", employee.getWorkingDays());
            billingMap.put("No of Absents",employee.getNoOfAbsents());
            billingMap.put("Address",employee.getAddress());
            billingMap.put("Country",employee.getCountry());
            billingMap.put("City",employee.getCity());
            billingMap.put("Nationality",employee.getNationality());
            billingMap.put("Job Title",employee.getJobTitle());
            billingMap.put("Department",employee.getDepartment());
            billingMap.put("Salary",employee.getSalary());
            billingMap.put("Housing",employee.getHousing());
            billingMap.put("Transportation",employee.getTransportation());
            billingMap.put("Other Allowance",employee.getOtherAllowance());
            billingMap.put("Total Amount",employee.getTotalAmount());

            result.add(billingMap);
        }

        return result;
    }

    public EmployeeDto toDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .createdAt(employee.getCreatedAt())
                .name(employee.getName())
                .firstname(employee.getFirstname())
                .lastname(employee.getLastname())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .country(employee.getCountry())
                .city(employee.getCity())
                .position(employee.getPosition())
                .nationality(employee.getNationality())
                .jobTitle(employee.getJobTitle())
                .department(employee.getDepartment())
                .salary(employee.getSalary())
                .housing(employee.getHousing())
                .mobile(employee.getMobile())
                .transportation(employee.getTransportation())
                .otherAllowance(employee.getOtherAllowance())
                .employeeNumber(employee.getEmployeeNumber())
                .passportFilePath(employee.getPassportFilePath())
                .idFilePath(employee.getIdFilePath())
                .personalEmail(employee.getPersonalEmail())
                .status(employee.getStatus())
                .totalAmount(employee.getTotalAmount())
                .createAsUser(employee.getCreateAsUser())
                .attachments(employee.getAttachments())
                .address(employee.getAddress())
                .dateOfJoining(employee.getDateOfJoining())
                .workingDays(employee.getWorkingDays())
                .noOfAbsents(employee.getNoOfAbsents())
                .build();
    }

    public Employee toEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .createdAt(employeeDto.getCreatedAt())
                .name(employeeDto.getName())
                .firstname(employeeDto.getFirstname())
                .lastname(employeeDto.getLastname())
                .email(employeeDto.getEmail())
                .phone(employeeDto.getPhone())
                .country(employeeDto.getCountry())
                .city(employeeDto.getCity())
                .position(employeeDto.getPosition())
                .nationality(employeeDto.getNationality())
                .jobTitle(employeeDto.getJobTitle())
                .department(employeeDto.getDepartment())
                .salary(employeeDto.getSalary())
                .personalEmail(employeeDto.getPersonalEmail())
                .housing(employeeDto.getHousing())
                .mobile(employeeDto.getMobile())
                .transportation(employeeDto.getTransportation())
                .otherAllowance(employeeDto.getOtherAllowance())
                .passportFilePath(employeeDto.getPassportFilePath())
                .idFilePath(employeeDto.getIdFilePath())
                .status(employeeDto.getStatus())
                .employeeNumber(employeeDto.getEmployeeNumber())
                .totalAmount(employeeDto.getTotalAmount())
                .createAsUser(employeeDto.getCreateAsUser())
                .attachments(employeeDto.getAttachments())
                .address(employeeDto.getAddress())
                .dateOfJoining(employeeDto.getDateOfJoining())
                .workingDays(employeeDto.getWorkingDays())
                .noOfAbsents(employeeDto.getNoOfAbsents())
                .build();
    }
}
