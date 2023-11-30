package com.salsel.service.impl;

import com.salsel.dto.TicketDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.*;
import com.salsel.repository.*;
import com.salsel.service.TicketService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentCategoryRepository departmentCategoryRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, UserRepository userRepository, CountryRepository countryRepository, CityRepository cityRepository, DepartmentRepository departmentRepository, DepartmentCategoryRepository departmentCategoryRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.departmentRepository = departmentRepository;
        this.departmentCategoryRepository = departmentCategoryRepository;
    }

    @Override
    @Transactional
    public TicketDto save(TicketDto ticketDto) {
        Ticket ticket = toEntity(ticketDto);
        ticket.setStatus(true);

        Country originCountry = countryRepository.findById(ticket.getOriginCountry().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Origin Country not found for id => %s", ticket.getOriginCity().getId())));

        Country destinationCountry = countryRepository.findById(ticket.getDestinationCountry().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Destination Country not found for id => %s", ticket.getDestinationCountry().getId())));

        City originCity = cityRepository.findById(ticket.getOriginCity().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Origin City not found for id => %s", ticket.getOriginCity().getId())));

        City destinationCity = cityRepository.findById(ticket.getDestinationCity().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Destination City not found for id => %s", ticket.getDestinationCity().getId())));

        User user = userRepository.findById(ticket.getCreatedBy().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("user not found for id => %s", ticket.getCreatedBy().getId())));

        Department department = departmentRepository.findById(ticket.getDepartment().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %s", ticket.getCreatedBy().getId())));

        DepartmentCategory departmentCategory = departmentCategoryRepository.findById(ticket.getDepartmentCategory().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department Category not found for id => %s", ticket.getDepartmentCategory().getId())));


        ticket.setOriginCity(originCity);
        ticket.setDestinationCity(destinationCity);
        ticket.setOriginCountry(originCountry);
        ticket.setDestinationCountry(destinationCountry);
        ticket.setCreatedBy(user);
        ticket.setDepartment(department);
        ticket.setDepartmentCategory(departmentCategory);

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

        existingTicket.setOriginCity(cityRepository.findById(ticketDto.getOriginCity().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Origin City not found for id => %d", ticketDto.getOriginCity().getId()))));

        existingTicket.setOriginCountry(countryRepository.findById(ticketDto.getOriginCountry().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Origin Country not found for id => %s", ticketDto.getOriginCity().getId()))));

        existingTicket.setDestinationCountry(countryRepository.findById(ticketDto.getDestinationCountry().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Destination Country not found for id => %s", ticketDto.getDestinationCountry().getId()))));

        existingTicket.setDestinationCity(cityRepository.findById(ticketDto.getDestinationCity().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Destination City not found for id => %s", ticketDto.getDestinationCity().getId()))));

        existingTicket.setCreatedBy(userRepository.findById(ticketDto.getCreatedBy().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("user not found for id => %s", ticketDto.getCreatedBy().getId()))));

        existingTicket.setDepartment(departmentRepository.findById(ticketDto.getDepartment().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department not found for id => %s", ticketDto.getCreatedBy().getId()))));

        existingTicket.setDepartmentCategory(departmentCategoryRepository.findById(ticketDto.getDepartmentCategory().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department Category not found for id => %s", ticketDto.getDepartmentCategory().getId()))));


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
