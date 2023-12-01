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
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
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

    private String category;
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
}
