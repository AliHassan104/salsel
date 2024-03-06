package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressBookDto {
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String uniqueId;
    private String name;
    private String contactNumber;
    private String country;
    private String city;
    private String address;
    private String streetName;
    private String district;
    private String refNumber;
    private String userType;
    private Boolean status;
}
