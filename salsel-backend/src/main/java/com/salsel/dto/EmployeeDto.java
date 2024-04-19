package com.salsel.dto;

import com.salsel.model.EmployeeAttachment;
import com.salsel.model.TicketAttachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {
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
    private List<EmployeeAttachment> attachments;
}
