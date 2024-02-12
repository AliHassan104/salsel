package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.model.TicketAttachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    private String deliveryStreetName;
    private String deliveryDistrict;
    private String pickupStreetName;
    private String pickupDistrict;
    private String name;
    private String weight;
    private String email;
    private String phone;
    private String textarea;
    private String airwayNumber;
    private String ticketType;
    private String ticketUrl;
    private String ticketSubCategory;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime pickupTime;

    private String ticketCategory;
    private String ticketFlag;
    private String assignedTo;
    private String originCountry;
    private String originCity;
    private String destinationCountry;
    private String destinationCity;
    private String createdBy;
    private String department;
    private String departmentCategory;
    private String ticketStatus;
    private Boolean status;
    private List<TicketAttachment> attachments;
}
