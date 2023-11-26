package com.salsel.dto;

import com.salsel.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceTypeDto {
    private Long id;
    private String code;
    private String name;
    private Boolean status;
    private ProductType productType;
}
