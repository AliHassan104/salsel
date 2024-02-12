package com.salsel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.dto.TicketAttachmentDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private String deliveryStreetName;
    private String deliveryDistrict;
    private String pickupStreetName;
    private String pickupDistrict;
    private String name;
    private String weight;
    private String email;
    private String phone;
    private String airwayNumber;
    private String ticketType;
    private String ticketUrl;
    private String ticketSubCategory;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime pickupTime;

    @Column(length = 1000)
    private String textarea;


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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id")
    private List<TicketAttachment> attachments;
}
