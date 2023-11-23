package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketDto {
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
    private LocalDate pickupDate;
    private LocalTime pickupTime;
    private String assignedTo;
    private String status;
    private String category;
    private String ticketFlag;
    private String createdDate;
    private String createdTime;
    private String ticketDepartment;
    private String categoryByDevelopment;

}
