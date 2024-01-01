package com.salsel.service;

import com.salsel.dto.TicketCommentsDto;

import java.util.List;

public interface TicketCommentsService {
    TicketCommentsDto save(TicketCommentsDto ticketCommentsDto);
    List<TicketCommentsDto> getAll(Boolean status);
    TicketCommentsDto findById(Long id);
    List<TicketCommentsDto> getAllCommentsByTicketId(Long ticketId);
    void deleteById(Long id);
    void setToActiveById(Long id);
    TicketCommentsDto update(Long id, TicketCommentsDto ticketCommentsDto);
}
