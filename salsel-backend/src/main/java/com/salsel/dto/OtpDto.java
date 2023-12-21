package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpDto {
    private Long id;
    private String resetCode;
    private LocalDateTime resetCodeTimestamp;
}
