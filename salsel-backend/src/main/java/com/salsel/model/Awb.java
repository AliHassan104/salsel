package com.salsel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "awb")
public class Awb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String shipperName;
    private String shipperContactNumber;
    private String originCountry;
    private String originCity;
    private String pickupAddress;
    private String shipperRefNumber;
    private String recipientsName;
    private String recipientsContactNumber;
    private String destinationCountry;
    private String destinationCity;
    private String deliveryAddress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime pickupTime;

    private String productType;
    private String serviceType;
    private Double pieces;
    private String content;
    private Double weight;
    private Double amount;
    private String currency;
    private String dutyAndTaxesBillTo;
    private Boolean status;
}
