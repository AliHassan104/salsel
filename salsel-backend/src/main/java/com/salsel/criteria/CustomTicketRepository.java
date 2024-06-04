package com.salsel.criteria;

import com.salsel.model.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface CustomTicketRepository {
    List<Ticket> findTickets(
            LocalDate startDate,
            LocalDate endDate,
            String ticketNumber,
            String ticketStatus,
            String ticketCategory,
            String ticketSubCategory,
            String department,
            String assignedTo
    );

}
