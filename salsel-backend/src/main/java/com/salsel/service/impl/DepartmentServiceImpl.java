package com.salsel.service.impl;

import com.salsel.dto.TicketDto;
import com.salsel.model.Ticket;
import com.salsel.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
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
    public TicketDto update(TicketDto ticketDto, Long id) throws Exception {
        return null;
    }

    @Override
    public List<TicketDto> findByPage(Integer pageNumber, Integer pageSize) {
        return null;
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
