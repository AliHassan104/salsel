package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.model.EmployeeAttachment;
import com.salsel.model.Role;
import com.salsel.model.TicketAttachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {
    private Long id;
    private LocalDate createdAt;
    private LocalDate dateOfJoining;
    private Double workingDays;
    private Double noOfAbsents;
    private String name;
    private String firstname;
    private String personalEmail;
    private String lastname;
    private String phone;
    private String email;
    private String city;
    private String country;
    private String nationality;
    private String jobTitle;
    private String address;
    private String department;
    private String employeeNumber;
    private Double salary;
    private Double housing;
    private Double transportation;
    private Double mobile;
    private Double otherAllowance;
    private String passportFilePath;
    private String idFilePath;
    private String position;
    private Double totalAmount;
    private Boolean createAsUser;
    private Boolean status;
    private List<EmployeeAttachment> attachments;
}
