package com.salsel.service;

import com.salsel.dto.TicketDto;

import java.util.List;

public interface DepartmentService {

    List<TicketDto> findAll();
    TicketDto findById(Long id);
    TicketDto save(TicketDto ticketDto);
    void delete(Long id);
    TicketDto update(TicketDto ticketDto , Long id) throws Exception;
    List<TicketDto> findByPage(Integer pageNumber, Integer pageSize);

}
