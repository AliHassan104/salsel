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
                .createdAt(ticket.getCreatedAt())
                .shipperName(ticket.getShipperName())

                .shipperContactNumber(ticket.getShipperContactNumber())
                .originCountry(ticket.getOriginCountry())
                .originCity(ticket.getOriginCity())

                .pickupAddress(ticket.getPickupAddress())
                .shipperRefNumber(ticket.getShipperRefNumber())
                .recipientsName(ticket.getRecipientsName())

                .recipientsContactNumber(ticket.getRecipientsContactNumber())
                .destinationCountry(ticket.getDestinationCountry())
                .destinationCity(ticket.getDestinationCity())

                .deliveryAddress(ticket.getDeliveryAddress())
                .pickupDate(ticket.getPickupDate())
                .pickupTime(ticket.getPickupTime())

                .assignedTo(ticket.getAssignedTo())
                .status(ticket.getStatus())
                .category(ticket.getCategory())

                .ticketFlag(ticket.getTicketFlag())
                .createdBy(ticket.getCreatedBy())
                .ticketDepartment(ticket.getTicketDepartment())
                .categoryByDevelopment(ticket.getCategoryByDevelopment())

                .build();
    }

    public Ticket toEntity(TicketDto ticketDto) {
        return Ticket.builder()
                .id(ticketDto.getId())
                .createdAt(ticketDto.getCreatedAt())
                .shipperName(ticketDto.getShipperName())

                .shipperContactNumber(ticketDto.getShipperContactNumber())
                .originCountry(ticketDto.getOriginCountry())
                .originCity(ticketDto.getOriginCity())

                .pickupAddress(ticketDto.getPickupAddress())
                .shipperRefNumber(ticketDto.getShipperRefNumber())
                .recipientsName(ticketDto.getRecipientsName())

                .recipientsContactNumber(ticketDto.getRecipientsContactNumber())
                .destinationCountry(ticketDto.getDestinationCountry())
                .destinationCity(ticketDto.getDestinationCity())

                .deliveryAddress(ticketDto.getDeliveryAddress())
                .pickupDate(ticketDto.getPickupDate())
                .pickupTime(ticketDto.getPickupTime())

                .assignedTo(ticketDto.getAssignedTo())
                .status(ticketDto.getStatus())
                .category(ticketDto.getCategory())

                .ticketFlag(ticketDto.getTicketFlag())
                .createdBy(ticketDto.getCreatedBy())
                .ticketDepartment(ticketDto.getTicketDepartment())
                .categoryByDevelopment(ticketDto.getCategoryByDevelopment())

                .build();
    }


}
