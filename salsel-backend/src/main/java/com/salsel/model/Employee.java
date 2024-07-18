package com.salsel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createdAt;
    private String name;
    private String firstname;
    private String lastname;
    private String phone;
    private String personalEmail;

    @Column(unique = true)
    private String employeeNumber;

    private LocalDate dateOfJoining;
    private Double workingDays;
    private Double noOfAbsents;

    private String email;
    private String city;
    private String address;
    private String country;
    private String nationality;
    private String jobTitle;
    private String department;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private List<EmployeeAttachment> attachments;
}
