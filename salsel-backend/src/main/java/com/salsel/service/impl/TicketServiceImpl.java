package com.salsel.service.impl;

import com.salsel.dto.TicketDto;
import com.salsel.model.Ticket;
import com.salsel.repository.TicketRepository;
import com.salsel.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<TicketDto> findAll() {
        return toDtoList(ticketRepository.findAll());
    }

    @Override
    public TicketDto findById(Long id) {
        return toDto(ticketRepository.findById(id).get());
    }

    @Override
    public TicketDto save(TicketDto ticketDto) {
        return toDto(ticketRepository.save(toEntity(ticketDto)));
    }

    @Override
    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public TicketDto update(TicketDto ticketDto, Long id) throws Exception {

        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()){
            return toDto(ticketRepository.save(toEntity(ticketDto)));
        }else {
            throw new Exception("");
        }

    }

    @Override
    public List<TicketDto> findPage(Integer pageNumber, Integer pageSize) {

        Page<Ticket> tickets = ticketRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id")));

        return toDtoList(tickets.toList());
    }

    public List<TicketDto> toDtoList(List<Ticket> ticketList){
        List<TicketDto> ticketDtoList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            TicketDto ticketDto = toDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    public TicketDto toDto(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getId())
                .build();
    }

    public Ticket toEntity(TicketDto ticketDto) {
        return Ticket.builder()
                .id(ticketDto.getId())
                .build();
    }


}
