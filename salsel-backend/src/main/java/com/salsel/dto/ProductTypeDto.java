package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductTypeDto {
    private Long id;
    private String code;
    private String name;
    private Boolean status;
}
