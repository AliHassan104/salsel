package com.salsel.service.impl;

import com.salsel.dto.TicketDto;
import com.salsel.repository.TicketRepository;
import com.salsel.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImplementation implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<TicketDto> findAll() {

        return null;
    }

    @Override
    public TicketDto findById(Long id) {
        return null;
    }

    @Override
    public TicketDto save(TicketDto ticketDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public TicketDto update(TicketDto ticketDto, Long id) {
        return null;
    }

    @Override
    public Page<TicketDto> findPage(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
