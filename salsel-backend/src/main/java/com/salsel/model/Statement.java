package com.salsel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
