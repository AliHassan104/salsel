package com.salsel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "billing")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;

    private Long customerAccountNumber;
    private Long airwayBillNo;

    @Column(unique = true)
    private String invoiceNo;

    private String address;
    private String country;
    private String city;
    private Long taxNo;
    private String taxInvoiceTo;
    private String invoiceType;
    private Long customerRef;
    private String product;
    private String serviceDetails;
    private Double charges;
    private Boolean status;
    private String billingStatus;
    private Double vatTax;
    private Double taxAmount;
}
