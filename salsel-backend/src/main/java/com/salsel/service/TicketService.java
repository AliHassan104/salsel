package com.salsel.service;

import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.PermissionDto;
import com.salsel.dto.TicketDto;
import com.salsel.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {

    Page<Ticket> findAll(SearchCriteria searchCriteria, Pageable pageable);
    TicketDto save(TicketDto ticketDto);
    List<TicketDto> getAll();
    TicketDto findById(Long id);
    void deleteById(Long id);
    TicketDto update(Long id, TicketDto ticketDto);
}
