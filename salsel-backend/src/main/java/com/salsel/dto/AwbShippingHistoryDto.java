package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.model.Awb;
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
public class AwbShippingHistoryDto {
    private Long id;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String awbStatus;
    private Boolean status;
    private Awb awb;
}
