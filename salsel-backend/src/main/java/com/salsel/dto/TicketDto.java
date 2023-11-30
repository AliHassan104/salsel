package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.model.*;
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
public class TicketDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String shipperName;
    private String shipperContactNumber;
    private String pickupAddress;
    private String shipperRefNumber;
    private String recipientName;
    private String recipientContactNumber;
    private String deliveryAddress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime pickupTime;

    private String ticketStatus;
    private Boolean status;
    private String category;
    private String ticketFlag;
    private String assignedTo;

    private Country originCountry;
    private City originCity;
    private Country destinationCountry;
    private City destinationCity;
    private User createdBy;
    private Department department;
    private DepartmentCategory departmentCategory;
}
