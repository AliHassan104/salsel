package com.salsel.service.impl;

import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.TicketDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Ticket;
import com.salsel.repository.TicketRepository;
import com.salsel.service.TicketService;
import com.salsel.specification.FilterSpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    FilterSpecification<Ticket> ticketFilterSpecification;

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

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket not found for id => %d", id)));


        ticket.setShipperName(ticketDto.getShipperName());
        ticket.setShipperContactNumber(ticketDto.getShipperName());
        ticket.setOriginCountry(ticketDto.getOriginCountry());
        ticket.setOriginCity(ticketDto.getOriginCity());

        ticket.setPickupAddress(ticketDto.getPickupAddress());
        ticket.setShipperRefNumber(ticketDto.getShipperRefNumber());
        ticket.setRecipientsName(ticketDto.getRecipientsName());

        ticket.setRecipientsContactNumber(ticketDto.getRecipientsContactNumber());
        ticket.setDestinationCountry(ticketDto.getDestinationCountry());
        ticket.setDestinationCity(ticketDto.getDestinationCity());

        ticket.setDeliveryAddress(ticketDto.getDeliveryAddress());
        ticket.setPickupAddress(ticketDto.getPickupAddress());
        ticket.setPickupTime(ticketDto.getPickupTime());

        ticket.setAssignedTo(ticketDto.getAssignedTo());
        ticket.setStatus(ticketDto.getStatus());
        ticket.setCategory(ticketDto.getCategory());

        ticket.setTicketFlag(ticketDto.getTicketFlag());
        ticket.setDepartment(ticketDto.getDepartment());
        ticket.setDepartmentCategory(ticketDto.getDepartmentCategory());

        Ticket updatedTicket = ticketRepository.save(ticket);
        return toDto(updatedTicket);

    }

    @Override
    public Page<TicketDto> findByPage(Integer pageNumber, Integer pageSize) {

        Page<Ticket> tickets = ticketRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id")));
        Page<TicketDto> ticketDtos = tickets.map(ticket -> toDto(ticket));
        return ticketDtos;
    }

    public List<TicketDto> toDtoList(List<Ticket> ticketList) {
        return ticketList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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
                .department(ticketDto.getDepartment())
                .departmentCategory(ticketDto.getDepartmentCategory())

                .build();
    }


}
