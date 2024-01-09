package com.salsel.dto;

import com.salsel.model.Ticket;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class TicketAttachmentDto {
    private Long id;
    private String filePath;
    private Ticket ticket;
}
