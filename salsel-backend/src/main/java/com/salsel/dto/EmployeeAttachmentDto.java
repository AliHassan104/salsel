package com.salsel.dto;

import com.salsel.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeAttachmentDto {
    private Long id;
    private String filePath;
    private Employee employee;
}
