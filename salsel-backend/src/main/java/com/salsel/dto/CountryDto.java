package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountryDto {
    private Long id;
    private Integer code;
    private String alphaCode;
    private String name;
    private Boolean status;
}
