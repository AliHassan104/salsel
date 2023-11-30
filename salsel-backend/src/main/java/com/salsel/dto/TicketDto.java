package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.model.Department;
import com.salsel.model.DepartmentCategory;
import com.salsel.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @CreationTimestamp
    private LocalDateTime createdAt;

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
    private Boolean ticketFlag;
    private User createdBy;
    private Department department;
    private DepartmentCategory departmentCategory;
}
