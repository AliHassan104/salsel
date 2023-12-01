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
    TicketDto findById(Long id);
    TicketDto save(TicketDto ticketDto);
    void delete(Long id);
    TicketDto update(TicketDto ticketDto , Long id) throws Exception;
    Page<TicketDto> findByPage(Integer pageNumber, Integer pageSize);
}
