package com.salsel.service;

import com.salsel.dto.TicketDto;

import java.util.List;

public interface TicketService {
    TicketDto save(TicketDto ticketDto);
    List<TicketDto> getAll();
    TicketDto findById(Long id);
    void deleteById(Long id);
    TicketDto update(Long id, TicketDto ticketDto);
}
