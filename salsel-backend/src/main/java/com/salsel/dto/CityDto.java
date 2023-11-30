package com.salsel.dto;

import com.salsel.model.Country;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CityDto {
    private Long id;
    private String name;
    private Boolean status;
    private Country country;
}
