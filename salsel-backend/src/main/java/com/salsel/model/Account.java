package com.salsel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createdAt;

    @Column(unique = true, nullable = false)
    private Long accountNumber;

    private String accountType;
    private String email;
    private String businessActivity;
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
