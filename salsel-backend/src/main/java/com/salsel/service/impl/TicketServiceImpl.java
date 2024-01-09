package com.salsel.service.impl;

import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.CustomUserDetail;
import com.salsel.dto.TicketDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Ticket;
import com.salsel.model.TicketAttachment;
import com.salsel.model.User;
import com.salsel.repository.TicketAttachmentRepository;
import com.salsel.repository.TicketRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.BucketService;
import com.salsel.service.TicketService;
import com.salsel.specification.FilterSpecification;
import com.salsel.utils.HelperUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketAttachmentRepository ticketAttachmentRepository;
    private final UserRepository userRepository;
    private final HelperUtils helperUtils;
    private final BucketService bucketService;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);

    public TicketServiceImpl(TicketRepository ticketRepository, TicketAttachmentRepository ticketAttachmentRepository, UserRepository userRepository, HelperUtils helperUtils, BucketService bucketService){
        this.ticketRepository = ticketRepository;
        this.ticketAttachmentRepository = ticketAttachmentRepository;
        this.userRepository = userRepository;
        this.helperUtils = helperUtils;
        this.bucketService = bucketService;
    }

    @Autowired
    FilterSpecification<Ticket> ticketFilterSpecification;

    @Override
    @Transactional
    public TicketDto save(TicketDto ticketDto, List<MultipartFile> pdfFiles) {
        Ticket ticket = toEntity(ticketDto);
        ticket.setStatus(true);
        ticket.setTicketStatus("Open");
        ticket.setTicketFlag("Normal");
        Ticket createdTicket = ticketRepository.save(ticket);

        // Save PDFs to S3 bucket if provided
        if (pdfFiles != null && !pdfFiles.isEmpty()) {
            String folderName = "Ticket_" + createdTicket.getId();
            List<String> savedPdfUrls = helperUtils.saveTicketPdfListToS3(pdfFiles, folderName);

            List<TicketAttachment> ticketAttachmentList = new ArrayList<>();
            for (String savedPdfUrl : savedPdfUrls) {
                TicketAttachment ticketAttachment = new TicketAttachment();
                ticketAttachment.setFilePath(savedPdfUrl);
                ticketAttachment.setTicket(createdTicket);
                ticketAttachmentList.add(ticketAttachment);
            }

            createdTicket.setAttachments(ticketAttachmentList);
            ticketAttachmentRepository.saveAll(ticketAttachmentList);
            logger.info("PDFs are uploaded to S3 in folder '{}'.", folderName);
        }
        return toDto(ticketRepository.save(createdTicket));
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
    public List<TicketDto> getTicketsByLoggedInUser(Boolean status) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            User user = userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));

            List<Ticket> ticketList = ticketRepository.findAllInDesOrderByEmailAndStatus(status,user.getEmail());
            List<TicketDto> ticketDtoList = new ArrayList<>();

            for (Ticket ticket : ticketList) {
                TicketDto ticketDto = toDto(ticket);
                ticketDtoList.add(ticketDto);
            }
            return ticketDtoList;
        }
        return null;
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
    public TicketDto update(Long id, TicketDto ticketDto, List<MultipartFile> pdfFiles, List<String> fileNames) {
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
        existingTicket.setDepartment(ticketDto.getDepartment());
        existingTicket.setDepartmentCategory(ticketDto.getDepartmentCategory());
        existingTicket.setDeliveryStreetName(ticketDto.getDeliveryStreetName());
        existingTicket.setDeliveryDistrict(ticketDto.getDeliveryDistrict());
        existingTicket.setPickupStreetName(ticketDto.getPickupStreetName());
        existingTicket.setPickupDistrict(ticketDto.getPickupDistrict());
        existingTicket.setName(ticketDto.getName());
        existingTicket.setWeight(ticketDto.getWeight());
        existingTicket.setEmail(ticketDto.getEmail());
        existingTicket.setPhone(ticketDto.getPhone());
        existingTicket.setTextarea(ticketDto.getTextarea());
        existingTicket.setAirwayNumber(ticketDto.getAirwayNumber());
        existingTicket.setTicketType(ticketDto.getTicketType());

        if (pdfFiles != null && !pdfFiles.isEmpty()) {
            // Delete existing files
            for (String fileName : fileNames) {
                bucketService.deleteFile(fileName);
            }

            // Save new files
            String folderName = "Ticket_" + existingTicket.getId();
            List<String> savedPdfUrls = helperUtils.saveTicketPdfListToS3(pdfFiles, folderName);

            // Create new attachments
            List<TicketAttachment> ticketAttachmentList = new ArrayList<>();
            for (String savedPdfUrl : savedPdfUrls) {
                TicketAttachment ticketAttachment = new TicketAttachment();
                ticketAttachment.setFilePath(savedPdfUrl);
                ticketAttachment.setTicket(existingTicket);
                ticketAttachmentList.add(ticketAttachment);
            }

            // Update existing ticket attachments
            existingTicket.getAttachments().clear();
            existingTicket.getAttachments().addAll(ticketAttachmentList);

            // Save attachments to the database
            ticketAttachmentRepository.saveAll(ticketAttachmentList);
            logger.info("PDFs are updated on S3 in folder '{}'.", folderName);
        }

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
                .deliveryDistrict(ticket.getDeliveryDistrict())
                .deliveryStreetName(ticket.getDeliveryStreetName())
                .pickupDistrict(ticket.getPickupDistrict())
                .pickupStreetName(ticket.getPickupStreetName())
                .name(ticket.getName())
                .ticketUrl(ticket.getTicketUrl())
                .email(ticket.getEmail())
                .weight(ticket.getWeight())
                .phone(ticket.getPhone())
                .textarea(ticket.getTextarea())
                .airwayNumber(ticket.getAirwayNumber())
                .ticketType(ticket.getTicketType())
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
                .ticketUrl(ticketDto.getTicketUrl())
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
                .deliveryDistrict(ticketDto.getDeliveryDistrict())
                .deliveryStreetName(ticketDto.getDeliveryStreetName())
                .pickupDistrict(ticketDto.getPickupDistrict())
                .pickupStreetName(ticketDto.getPickupStreetName())
                .name(ticketDto.getName())
                .email(ticketDto.getEmail())
                .weight(ticketDto.getWeight())
                .phone(ticketDto.getPhone())
                .textarea(ticketDto.getTextarea())
                .airwayNumber(ticketDto.getAirwayNumber())
                .ticketType(ticketDto.getTicketType())
                .build();
    }
}