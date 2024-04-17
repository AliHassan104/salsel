package com.salsel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private Double otherAllowance;
    private Long userId;
    private Boolean status;
}
