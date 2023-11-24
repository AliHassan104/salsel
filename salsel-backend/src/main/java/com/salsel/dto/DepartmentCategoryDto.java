package com.salsel.dto;

import com.salsel.model.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DepartmentCategoryDto {

    private Long id;

    private String name;

    private String code;

    private Department department;
}
