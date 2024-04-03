package com.salsel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.model.Awb;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AwbStatusCommentDto {
    private Long id;

    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String awbStatus;
    private String commentBy;
    private String comment;
    private Boolean status;
    private Awb awb;
}
