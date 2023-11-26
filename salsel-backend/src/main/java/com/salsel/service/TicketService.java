package com.salsel.service;

import com.salsel.dto.PermissionDto;
import com.salsel.dto.TicketDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {

    List<TicketDto> findAll();
    TicketDto findById(Long id);
    TicketDto save(TicketDto ticketDto);
    void delete(Long id);
    TicketDto update(TicketDto ticketDto , Long id) throws Exception;
    Page<TicketDto> findPage(Integer pageNumber, Integer pageSize);
}
