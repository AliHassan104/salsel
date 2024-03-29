package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountDto {
    private Long id;
    private LocalDate createdAt;
    private String accountType;
    private String email;
    private String businessActivity;
    private Long accountNumber;
    private String customerName;
    private String projectName;
    private String tradeLicenseNo;
    private String taxDocumentNo;
    private String county;
    private String city;
    private String address;
    private String custName;
    private String contactNumber;
    private String billingPocName;
    private String salesAgentName;
    private String salesRegion;
    private String accountUrl;
    private Boolean status;
}
