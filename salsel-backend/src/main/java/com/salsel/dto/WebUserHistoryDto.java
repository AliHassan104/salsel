package com.salsel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WebUserHistoryDto {
    private Long id;
    private String browser;
    private String ip;
    private String timeZone;
    private String location;
    private Long userId;
}
