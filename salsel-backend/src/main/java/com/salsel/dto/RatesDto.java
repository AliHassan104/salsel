package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RatesDto {
    private Long id;
    private String fromCountry;
    private String toCountry;
    private String product;
    private Double weightRangeFrom;
    private Double weightRangeTo;
    private Double charges;
    private Double additionalCharges;
    private Boolean status;
}
