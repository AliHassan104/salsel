package com.salsel.service.impl;

import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.CustomUserDetail;
import com.salsel.dto.TicketDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Ticket;
import com.salsel.model.User;
import com.salsel.repository.TicketRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.TicketService;
import com.salsel.specification.FilterSpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    FilterSpecification<Ticket> ticketFilterSpecification;

    @Override
    @Transactional
    public TicketDto save(TicketDto ticketDto) {
        Ticket ticket = toEntity(ticketDto);
        ticket.setStatus(true);
        Ticket createdTicket = ticketRepository.save(ticket);
        return toDto(createdTicket);
    }

    @Override
    public Page<Ticket> findAll(SearchCriteria searchCriteria, Pageable pageable) {

        Optional<Page<Ticket>> tickets = null;

        if(StringUtils.isBlank(searchCriteria.getSearchText())){
            tickets = Optional.of(ticketRepository.findAll(pageable));
        }else{
            Specification<Ticket> ticketSpecification = this.ticketFilterSpecification.getSearchSpecification(Ticket.class, searchCriteria);
            tickets = Optional.of(this.ticketRepository.findAll(ticketSpecification, pageable));
        }

//        return toDtoList(ticketRepository.findAll());
        return tickets.isPresent() ? tickets.get() : null;
    }

    @Override
    public List<TicketDto> getAll(Boolean status) {
        List<Ticket> ticketList = ticketRepository.findAllInDesOrderByIdAndStatus(status);
        List<TicketDto> ticketDtoList = new ArrayList<>();

        for (Ticket ticket : ticketList) {
            TicketDto ticketDto = toDto(ticket);
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    @Override
    public List<TicketDto> getTicketsByLoggedInUser(String createdByUser, Boolean status) {
        User user = userRepository.findByNameAndStatusIsTrue(createdByUser)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));

        List<Ticket> ticketList = ticketRepository.findAllInDesOrderByNameAndStatus(status,user.getName());
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
    public void setToActiveById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket not found for id => %d", id)));
        ticketRepository.setStatusActive(ticket.getId());
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