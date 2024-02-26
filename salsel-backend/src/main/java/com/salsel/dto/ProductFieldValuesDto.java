package com.salsel.dto;

import com.salsel.model.ProductField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFieldValuesDto {
    private Long id;
    private String name;
    private String status;
}
