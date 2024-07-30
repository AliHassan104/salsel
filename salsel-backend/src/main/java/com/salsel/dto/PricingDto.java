package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PricingDto {
    private Long id;
    private String country;
    private String product;
    private String code;
    private Double weightRangeFrom;
    private Double weightRangeTo;
    private Double charges;
    private Double additionalCharges;
}
