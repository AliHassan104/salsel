package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillingDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;
    private Long customerAccountNumber;
    private Long airwayBillNo;
    private String invoiceNo;
    private String address;
    private String country;
    private String city;
    private String taxNo;
    private String taxInvoiceTo;
    private String invoiceType;
    private String customerRef;
    private String product;
    private String serviceDetails;
    private Double charges;
    private Boolean status;
    private String billingStatus;
    private Double vatTax;
    private Double taxAmount;
    private Boolean isEmailSend;
}
