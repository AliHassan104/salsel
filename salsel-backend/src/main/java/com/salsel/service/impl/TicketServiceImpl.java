package com.salsel.service.impl;

import com.salsel.dto.TicketDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Ticket;
import com.salsel.repository.TicketRepository;
import com.salsel.service.TicketService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public TicketDto save(TicketDto ticketDto) {
        Ticket ticket = toEntity(ticketDto);
        ticket.setStatus(true);
        Ticket createdTicket = ticketRepository.save(ticket);
        return toDto(createdTicket);
    }

    @Override
    public List<TicketDto> getAll() {
        List<Ticket> ticketList = ticketRepository.findAllInDesOrderByIdAndStatus();
        List<TicketDto> ticketDtoList = new ArrayList<>();

        for (Ticket ticket : ticketList) {
            TicketDto ticketDto = toDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    @Override
    public TicketDto findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket not found for id => %d", id)));
        return toDto(ticket);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket not found for id => %d", id)));
        ticketRepository.setStatusInactive(ticket.getId());
    }

    @Override
    @Transactional
    public TicketDto update(Long id, TicketDto ticketDto) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket not found for id => %d", id)));

        existingTicket.setShipperName(ticketDto.getShipperName());
        existingTicket.setShipperContactNumber(ticketDto.getShipperContactNumber());
        existingTicket.setPickupAddress(ticketDto.getPickupAddress());
        existingTicket.setShipperRefNumber(ticketDto.getShipperRefNumber());
        existingTicket.setRecipientName(ticketDto.getRecipientName());
        existingTicket.setRecipientContactNumber(ticketDto.getRecipientContactNumber());
        existingTicket.setDeliveryAddress(ticketDto.getDeliveryAddress());
        existingTicket.setPickupDate(ticketDto.getPickupDate());
        existingTicket.setPickupTime(ticketDto.getPickupTime());
        existingTicket.setTicketStatus(ticketDto.getTicketStatus());
        existingTicket.setCategory(ticketDto.getCategory());
        existingTicket.setTicketFlag(ticketDto.getTicketFlag());
        existingTicket.setAssignedTo(ticketDto.getAssignedTo());
        existingTicket.setOriginCity(ticketDto.getOriginCity());
        existingTicket.setOriginCountry(ticketDto.getOriginCountry());
        existingTicket.setDestinationCountry(ticketDto.getDestinationCountry());
        existingTicket.setDestinationCity(ticketDto.getDestinationCity());
        existingTicket.setCreatedBy(ticketDto.getCreatedBy());
        existingTicket.setDepartment(ticketDto.getDepartment());
        existingTicket.setDepartmentCategory(ticketDto.getDepartmentCategory());

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return toDto(updatedTicket);
    }

    public TicketDto toDto(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getId())
                .createdAt(ticket.getCreatedAt())
                .shipperName(ticket.getShipperName())
                .shipperContactNumber(ticket.getShipperContactNumber())
                .pickupAddress(ticket.getPickupAddress())
                .shipperRefNumber(ticket.getShipperRefNumber())
                .recipientName(ticket.getRecipientName())
                .recipientContactNumber(ticket.getRecipientContactNumber())
                .deliveryAddress(ticket.getDeliveryAddress())
                .pickupDate(ticket.getPickupDate())
                .pickupTime(ticket.getPickupTime())
                .ticketStatus(ticket.getTicketStatus())
                .status(ticket.getStatus())
                .category(ticket.getCategory())
                .ticketFlag(ticket.getTicketFlag())
                .originCity(ticket.getOriginCity())
                .originCountry(ticket.getOriginCountry())
                .destinationCity(ticket.getDestinationCity())
                .destinationCountry(ticket.getDestinationCountry())
                .createdBy(ticket.getCreatedBy())
                .assignedTo(ticket.getAssignedTo())
                .department(ticket.getDepartment())
                .departmentCategory(ticket.getDepartmentCategory())
                .build();
    }

    public Ticket toEntity(TicketDto ticketDto) {
        return Ticket.builder()
                .id(ticketDto.getId())
                .createdAt(ticketDto.getCreatedAt())
                .shipperName(ticketDto.getShipperName())
                .shipperContactNumber(ticketDto.getShipperContactNumber())
                .pickupAddress(ticketDto.getPickupAddress())
                .shipperRefNumber(ticketDto.getShipperRefNumber())
                .recipientName(ticketDto.getRecipientName())
                .recipientContactNumber(ticketDto.getRecipientContactNumber())
                .deliveryAddress(ticketDto.getDeliveryAddress())
                .pickupDate(ticketDto.getPickupDate())
                .pickupTime(ticketDto.getPickupTime())
                .ticketStatus(ticketDto.getTicketStatus())
                .status(ticketDto.getStatus())
                .category(ticketDto.getCategory())
                .ticketFlag(ticketDto.getTicketFlag())
                .originCity(ticketDto.getOriginCity())
                .originCountry(ticketDto.getOriginCountry())
                .destinationCity(ticketDto.getDestinationCity())
                .destinationCountry(ticketDto.getDestinationCountry())
                .createdBy(ticketDto.getCreatedBy())
                .assignedTo(ticketDto.getAssignedTo())
                .department(ticketDto.getDepartment())
                .departmentCategory(ticketDto.getDepartmentCategory())
                .build();
    }
}
