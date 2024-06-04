package com.salsel.service.impl;

import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.CustomUserDetail;
import com.salsel.dto.TicketDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Role;
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
import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        ticket.setAssignedTo("ROLE_CUSTOMER_SERVICE_AGENT");

        Optional<Ticket> latestTicket = ticketRepository.findTicketByLatestId();
        if (latestTicket.isPresent()) {
            Ticket existingTicket = latestTicket.get();
            String currentTicketNumber = existingTicket.getTicketNumber();
            Long ticketNumber = Long.parseLong(currentTicketNumber);

            ticketNumber++;

            String newTicketNumber;
            if (currentTicketNumber.matches("^0\\d+$")) {
                newTicketNumber = String.format("%0" + currentTicketNumber.length() + "d", ticketNumber); // Maintain leading zeros
            } else {
                newTicketNumber = String.valueOf(ticketNumber); // No leading zeros
            }
            ticket.setTicketNumber(newTicketNumber);
        }else{
            ticket.setTicketNumber("0001");
        }


        Ticket createdTicket = ticketRepository.save(ticket);

        // Save PDFs to S3 bucket if provided
        if (pdfFiles != null && !pdfFiles.isEmpty()) {
            String folderName = "Ticket_" + createdTicket.getId();
            List<String> savedPdfUrls = helperUtils.savePdfListToS3(pdfFiles, folderName, "Ticket");

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
    public List<TicketDto> getAllByTicketStatus(String status) {
        List<Ticket> ticketList = ticketRepository.findAllInDesOrderByIdAndTicketStatus(status);
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
    public List<TicketDto> getTicketsByLoggedInUserAndRole(Boolean status) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            User user = userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));

            List<TicketDto> ticketDtoList = new ArrayList<>();


            for (Role role : user.getRoles()) {
                String roleName = role.getName();
                List<Ticket> ticketList = ticketRepository.findAllInDesOrderByCreatedByOrAssignedToAndStatus(status, user.getEmail(),roleName,user.getEmail());

                for (Ticket ticket : ticketList) {
                    TicketDto ticketDto = toDto(ticket);
                    ticketDtoList.add(ticketDto);
                }
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
    public LocalDate getMinCreatedAt() {
        return ticketRepository.findMinCreatedAt();
    }

    @Override
    public LocalDate getMaxCreatedAt() {
        return ticketRepository.findMaxCreatedAt();
    }

    @Override
    public Map<String, Long> getStatusCounts() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Add logic to get counts for true status
        statusCounts.put("active", ticketRepository.countByStatus(true));

        // Add logic to get counts for false status
        statusCounts.put("inactive", ticketRepository.countByStatus(false));

        return statusCounts;
    }


    @Override
    public Map<String, Long> getStatusCountsBasedOnLoggedInUser() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Get the logged-in user's email
        String loggedInUserEmail = getLoggedInUserEmail();
        String loggedInUserRole = getLoggedInUserRole();

        // Add logic to get counts based on different AWB statuses for the logged-in user
        statusCounts.put("active", ticketRepository.countByStatusAndCreatedByOrAssignedTo(true,  loggedInUserEmail,loggedInUserRole));
        statusCounts.put("inactive", ticketRepository.countByStatusAndCreatedByOrAssignedTo(false,  loggedInUserEmail,loggedInUserRole));

        return statusCounts;
    }

    @Override
    public Long getTicketCount() {
        return ticketRepository.countByLoggedInUser();
    }

    @Override
    public List<TicketDto> findTicketsForExcel(LocalDate startDate, LocalDate endDate, String ticketNumber, String ticketStatus, String ticketCategory, String ticketSubCategory, String department, String assignedTo) {
        List<Ticket> tickets = ticketRepository.findTickets(startDate, endDate, ticketNumber, ticketStatus, ticketCategory, ticketSubCategory, department, assignedTo);
        return tickets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private String getLoggedInUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            return ((CustomUserDetail) principal).getEmail();
        }
        return null;
    }

    private String getLoggedInUserRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            CustomUserDetail userDetails = (CustomUserDetail) principal;

            if (userDetails != null && userDetails.getAuthorities() != null && userDetails.getAuthorities().size() > 0) {

                return userDetails.getAuthorities().iterator().next().getAuthority();
            }
        }

        return null;
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
        existingTicket.setTicketCategory(ticketDto.getTicketCategory());
        existingTicket.setTicketSubCategory(ticketDto.getTicketSubCategory());
        existingTicket.setTicketFlag(ticketDto.getTicketFlag());
        existingTicket.setAssignedTo(ticketDto.getAssignedTo());
        existingTicket.setOriginCity(ticketDto.getOriginCity());
        existingTicket.setOriginCountry(ticketDto.getOriginCountry());
        existingTicket.setDestinationCountry(ticketDto.getDestinationCountry());
        existingTicket.setDestinationCity(ticketDto.getDestinationCity());
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
            List<String> savedPdfUrls = helperUtils.savePdfListToS3(pdfFiles, folderName, "Ticket");

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
        }else {
            // No new files attached, delete existing files
            for (String fileName : fileNames) {
                bucketService.deleteFile(fileName);
            }

            // Clear existing attachments in the database
            existingTicket.getAttachments().clear();
            logger.info("Existing PDFs are deleted.");
        }

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return toDto(updatedTicket);
    }

    @Override
    public List<TicketDto> getTicketsBetweenDates(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Get logged-in user's email and role
        String loggedInUserEmail = getLoggedInUserEmail();
        String loggedInUserRole = getLoggedInUserRole();

        boolean isAdminOrCustomerServiceAgent = "ROLE_ADMIN".equals(loggedInUserRole) || "ROLE_CUSTOMER_SERVICE_AGENT".equals(loggedInUserRole);

        if(isAdminOrCustomerServiceAgent){
            return ticketRepository.findAllByCreatedAtBetween(startDateTime, endDateTime)
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }else{
            return ticketRepository.findAllByCreatedAtBetweenAndLoggedInUser(startDateTime,endDateTime,loggedInUserEmail,loggedInUserRole,loggedInUserEmail)
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }
    }



    public TicketDto toDto(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getId())
                .createdAt(ticket.getCreatedAt())
                .ticketNumber(ticket.getTicketNumber())
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
                .ticketCategory(ticket.getTicketCategory())
                .ticketSubCategory(ticket.getTicketSubCategory())
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
                .attachments(ticket.getAttachments())
                .build();
    }

    public Ticket toEntity(TicketDto ticketDto) {
        return Ticket.builder()
                .id(ticketDto.getId())
                .createdAt(ticketDto.getCreatedAt())
                .ticketNumber(ticketDto.getTicketNumber())
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
                .ticketCategory(ticketDto.getTicketCategory())
                .ticketSubCategory(ticketDto.getTicketSubCategory())
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
                .attachments(ticketDto.getAttachments())
                .build();
    }
}