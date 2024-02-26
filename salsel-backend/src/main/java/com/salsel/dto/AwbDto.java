package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AwbDto {
    private Long id;
    private Long uniqueNumber;
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String shipperName;
    private String shipperContactNumber;
    private String originCountry;
    private String originCity;
    private String pickupAddress;
    private String pickupStreetName;
    private String pickupDistrict;
    private String shipperRefNumber;
    private String recipientsName;
    private String recipientsContactNumber;
    private String destinationCountry;
    private String destinationCity;
    private String deliveryAddress;
    private String deliveryStreetName;
    private String deliveryDistrict;
    private String accountNumber;
    private String serviceTypeCode;
    private String createdBy;
    private String assignedTo;
    private String assignedToUser;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime pickupTime;

    private String productType;
    private String serviceType;
    private String requestType;
    private Double pieces;
    private String content;
    private Double weight;
    private Double amount;
    private String currency;
    private String dutyAndTaxesBillTo;
    private Boolean status;
    private Boolean emailFlag;
    private String awbUrl;
    private String awbStatus;
}
