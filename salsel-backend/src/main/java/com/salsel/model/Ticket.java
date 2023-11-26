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

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
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

    @OneToOne
    private User createdBy;
    private String department;
    private String departmentCategory;
}
