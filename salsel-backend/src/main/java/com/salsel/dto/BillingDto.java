package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillingDto {
    private Long id;
    private Long accountNumber;
    private Long shipmentNumber;
    private String product;
    private String serviceDetails;
    private Double charges;
    private Boolean status;
    private String billingStatus;
}
