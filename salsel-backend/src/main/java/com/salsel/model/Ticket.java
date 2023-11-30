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

    private String ticketStatus;
    private Boolean status;

    private String category;
    private String ticketFlag;
    private String assignedTo;


    @ManyToOne
    @JoinColumn(name = "origin_country_id")
    private Country originCountry;

    @ManyToOne
    @JoinColumn(name = "origin_city_id")
    private City originCity;

    @ManyToOne
    @JoinColumn(name = "destination_country_id")
    private Country destinationCountry;

    @ManyToOne
    @JoinColumn(name = "destination_city_id")
    private City destinationCity;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "department_category_id")
    private DepartmentCategory departmentCategory;
}
