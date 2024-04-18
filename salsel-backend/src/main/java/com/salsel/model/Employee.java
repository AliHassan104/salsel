package com.salsel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Long userId;
    private Boolean status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private List<EmployeeAttachment> attachments;
}
