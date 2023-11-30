package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.dto.projectEnums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductFieldDto {
    private Long id;
    private String name;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate created_at;

    private Integer sequence;
    private Type type;
    private List<ProductFieldValuesDto> productFieldValuesList;
}
