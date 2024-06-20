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
public class StatementDto {
    private Long id;
    private LocalDate createdAt;
    private String customerName;
    private Long accountNumber;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String invoiceType;
    private String customerRef;
    private Double debit;
    private Double credit;
    private Double net;
    private Boolean isEmailSend;
    private String url;
    private Boolean status;
}
