package com.salsel.service.impl;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.salsel.dto.AwbDto;
import com.salsel.dto.CustomUserDetail;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.model.AwbShippingHistory;
import com.salsel.model.Role;
import com.salsel.model.User;
import com.salsel.repository.AwbRepository;
import com.salsel.repository.AwbShippingHistoryRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.AwbService;
import com.salsel.service.AwbShippingHistoryService;
import com.salsel.service.CodeGenerationService;
import com.salsel.service.PdfGenerationService;
import com.salsel.utils.AssignmentEmailsUtils;
import com.salsel.utils.HelperUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.salsel.constants.AwbStatusConstants.*;

@Service
@EnableAsync
public class AwbServiceImpl implements AwbService {

    @Value("${spring.mail.username}")
    private String sender;
    private final AwbRepository awbRepository;
    private final HelperUtils helperUtils;
    private final UserRepository userRepository;
    private final CodeGenerationService codeGenerationService;
    private final AssignmentEmailsUtils assignmentEmailsUtils;
    private final AwbShippingHistoryRepository awbShippingHistoryRepository;
    private final AwbShippingHistoryService awbShippingHistoryService;
    private final PdfGenerationService pdfGenerationService;

    public AwbServiceImpl(AwbRepository awbRepository, HelperUtils helperUtils, UserRepository userRepository, CodeGenerationService codeGenerationService, AssignmentEmailsUtils assignmentEmailsUtils, AwbShippingHistoryRepository awbShippingHistoryRepository, AwbShippingHistoryService awbShippingHistoryService, PdfGenerationService pdfGenerationService) {
        this.awbRepository = awbRepository;
        this.helperUtils = helperUtils;
        this.userRepository = userRepository;
        this.codeGenerationService = codeGenerationService;
        this.assignmentEmailsUtils = assignmentEmailsUtils;
        this.awbShippingHistoryRepository = awbShippingHistoryRepository;
        this.awbShippingHistoryService = awbShippingHistoryService;
        this.pdfGenerationService = pdfGenerationService;
    }

    @Override
    @Transactional
    public AwbDto save(AwbDto awbDto) {
        try {
            Long maxUniqueNumber = awbRepository.findMaxUniqueNumber();
            awbDto.setUniqueNumber(maxUniqueNumber == null ? 900000001L : maxUniqueNumber + 10);

            Awb awb = toEntity(awbDto);
            awb.setAwbStatus("AWB Created");
            awb.setStatus(true);
            awb.setEmailFlag(false);

            if(awb.getAssignedToUser() != null){
                User user = userRepository.findById(awb.getAssignedToUser().getId())
                        .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", awb.getAssignedToUser().getId())));
                awb.setAssignedToUser(user);
            }

            Awb createdAwb = awbRepository.save(awb);
            Long awbId = createdAwb.getId();

            awbShippingHistoryService.addAwbShippingHistory(createdAwb);

            codeGenerationService.generateBarcode(awb.getUniqueNumber().toString(), awbId);
            codeGenerationService.generateBarcodeVertical(awb.getUniqueNumber().toString(), awbId);
            codeGenerationService.generateQRCode(awb.getUniqueNumber().toString(), awbId);

            sendEmailAsync(createdAwb);

            return toDto(createdAwb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecordNotFoundException("Error occurred while processing the request");
        }
    }

    @Override
    public List<AwbDto> getAll(Boolean status) {
        List<Awb> awbList = awbRepository.findAllInDesOrderByIdAndStatus(status);
        List<AwbDto> awbDtoList = new ArrayList<>();

        for (Awb awb : awbList) {
            AwbDto awbDto = toDto(awb);
            awbDtoList.add(awbDto);
        }
        return awbDtoList;
    }

    @Override
    public byte[] downloadAwbPdf(String fileName, Long awbId) {
        Awb awb = awbRepository.findById(awbId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", awbId)));

        Model model = new ExtendedModelMap();
        model.addAttribute("awbId", awbId);
        model.addAttribute("weight", awb.getWeight());
        model.addAttribute("shipperName", awb.getShipperName());
        model.addAttribute("pickupAddress", awb.getPickupAddress());
        model.addAttribute("recipientsName", awb.getRecipientsName());
        model.addAttribute("deliveryAddress", awb.getDeliveryAddress());
        model.addAttribute("shipperRefNumber", awb.getShipperRefNumber());
        model.addAttribute("currency", awb.getCurrency());
        model.addAttribute("originCountry", awb.getOriginCountry());
        model.addAttribute("originCity", awb.getOriginCity());
        model.addAttribute("destinationCountry", awb.getDestinationCountry());
        model.addAttribute("destinationCity", awb.getDestinationCity());
        model.addAttribute("shipperContactNumber", awb.getShipperContactNumber());
        model.addAttribute("recipientsContactNumber", awb.getRecipientsContactNumber());
        model.addAttribute("pickupAddress", awb.getPickupAddress());
        model.addAttribute("deliveryAddress", awb.getDeliveryAddress());
        model.addAttribute("amount", awb.getAmount());
        model.addAttribute("content", awb.getContent());
        model.addAttribute("requestType", awb.getRequestType());
        model.addAttribute("dutyAndTaxesBillTo", awb.getDutyAndTaxesBillTo());
        model.addAttribute("productType", awb.getProductType());
        model.addAttribute("serviceType", awb.getServiceType());
        model.addAttribute("serviceTypeCode",awb.getServiceTypeCode());
        model.addAttribute("deliveryDistrict",awb.getDeliveryDistrict());
        model.addAttribute("deliveryStreetName",awb.getDeliveryStreetName());
        model.addAttribute("pickupDistrict",awb.getPickupDistrict());
        model.addAttribute("pickupStreetName",awb.getPickupStreetName());

        String formatUniqueNumber = String.format("%,d", awb.getUniqueNumber()).replace(",", " ");
        model.addAttribute("uniqueNumber", formatUniqueNumber);

        int pieces = awb.getPieces().intValue();
        int count = pieces;
        int checkPieces = 1;
        String number = null;
        try (ByteArrayOutputStream mergedOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, mergedOutputStream);
            document.open();

            for (int i = 0; i < count; i++) {
                model.addAttribute("pieces", awb.getPieces().intValue());

                if(checkPieces != awb.getPieces()){
                    model.addAttribute("pieceNumber", checkPieces);
                    checkPieces++;
                }
                else{
                    model.addAttribute("pieceNumber", awb.getPieces().intValue());
                }

                byte[] individualPdf = pdfGenerationService.generatePdf("Awb", model, awbId);

                if(checkPieces != 1){
                    number = " / " + checkPieces;
                    model.addAttribute("number", number);
                }

                PdfReader reader = new PdfReader(new ByteArrayInputStream(individualPdf));
                for (int pageNum = 1; pageNum <= reader.getNumberOfPages(); pageNum++) {
                    copy.addPage(copy.getImportedPage(reader, pageNum));
                }
                pieces--;
            }

            document.close();
            return mergedOutputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error merging PDFs: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AwbDto> getAwbByLoggedInUser(Boolean status) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            User user = userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));

            List<Awb> awbList = awbRepository.findAllInDesOrderByEmailAndStatus(status,user.getEmail());
            List<AwbDto> awbDtoList = new ArrayList<>();

            for (Awb awb : awbList) {
                AwbDto awbDto = toDto(awb);
                awbDtoList.add(awbDto);
            }
            return awbDtoList;
        }
        return null;
    }

    @Override
    public List<AwbDto> getAwbByAssignedUser(String user, Boolean status) {
        List<Awb> awbList = awbRepository.findAllAwbByAssignedUserAndStatus(status, user);
        List<AwbDto> awbDtoList = new ArrayList<>();

        for (Awb awb : awbList) {
            AwbDto awbDto = toDto(awb);
            awbDtoList.add(awbDto);
        }
        return awbDtoList;
    }

    @Override
    public List<AwbDto> getAwbByAssignedUserAndStatus(Long userId, String status) {
        List<Awb> awbList = awbRepository.findAllInDesOrderByAssignedUserAndIdAndAwbStatus(status, userId);
        List<AwbDto> awbDtoList = new ArrayList<>();

        for (Awb awb : awbList) {
            AwbDto awbDto = toDto(awb);
            awbDtoList.add(awbDto);
        }
        return awbDtoList;
    }

    @Override
    public List<AwbDto> getAllAwbByAssignedUser(Long userId) {
        List<Awb> awbList = awbRepository.findAllInDesOrderByAssignedUserId(userId);
        List<AwbDto> awbDtoList = new ArrayList<>();

        for (Awb awb : awbList) {
            AwbDto awbDto = toDto(awb);
            awbDtoList.add(awbDto);
        }
        return awbDtoList;
    }


    @Override
    public List<AwbDto> getAwbByLoggedInUserRole(Boolean status) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            User user = userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));

            List<AwbDto> awbDtoList = new ArrayList<>();

            for (Role role : user.getRoles()) {
                String roleName = role.getName();
                List<Awb> awbList = awbRepository.findAllInDesOrderByRoleAndStatus(status, roleName);

                for (Awb awb : awbList) {
                    AwbDto awbDto = toDto(awb);
                    awbDtoList.add(awbDto);
                }
            }
            return awbDtoList;
        }
        return null;
    }

    @Override
    public List<AwbDto> getAwbByLoggedInUserAndRole(Boolean status) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            User user = userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));

            List<AwbDto> awbDtoList = new ArrayList<>();


            for (Role role : user.getRoles()) {
                String roleName = role.getName();
                List<Awb> awbList = awbRepository.findAllInDesOrderByCreatedByOrAssignedToAndStatus(status, user.getEmail(),roleName);

                for (Awb awb : awbList) {
                    AwbDto awbDto = toDto(awb);
                    awbDtoList.add(awbDto);
                }
            }
            return awbDtoList;
        }
        return null;
    }

    @Override
    public AwbDto findById(Long id) {
        Awb awb = awbRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", id)));
        return toDto(awb);
    }

    @Override
    public AwbDto findByUniqueNumber(Long uniqueNumber) {
        Awb awb = awbRepository.findByUniqueNumber(uniqueNumber)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for UniqueNumber => %d", uniqueNumber)));
        return toDto(awb);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Awb awb = awbRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", id)));
        awbRepository.setStatusInactive(awb.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Awb awb = awbRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", id)));
        awbRepository.setStatusActive(awb.getId());
    }

    @Override
    @Transactional
    public AwbDto update(Long id, AwbDto awbDto) {
        Awb existingAwb = awbRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", id)));

        existingAwb.setAssignedTo(awbDto.getAssignedTo());

        Awb updatedAwb = awbRepository.save(existingAwb);
        return toDto(updatedAwb);
    }

    @Override
    @Transactional
    public AwbDto updateAwbStatusOnScan(Long uniqueNumber, String awbStatus) {
        Awb existingAwb = awbRepository.findByUniqueNumber(uniqueNumber)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for Tracking Number => %d", uniqueNumber)));

        User currentUser = helperUtils.getCurrentUser();
        existingAwb.setAssignedToUser(currentUser);
        existingAwb.setCreatedBy(currentUser.getEmail());

        switch (awbStatus) {
            case AWB_CREATED:
                existingAwb.setAwbStatus(AWB_CREATED);
                break;
            case PICKED_UP:
                existingAwb.setAwbStatus(PICKED_UP);
                break;
            case ARRIVED_IN_STATION:
                existingAwb.setAwbStatus(ARRIVED_IN_STATION);
                break;
            case HELD_IN_STATION:
                existingAwb.setAwbStatus(HELD_IN_STATION);
                break;
            case DEPART_FROM_STATION:
                existingAwb.setAwbStatus(DEPART_FROM_STATION);
                break;
            case ARRIVED_IN_HUB:
                existingAwb.setAwbStatus(ARRIVED_IN_HUB);
                break;
            case DEPART_FROM_HUB:
                existingAwb.setAwbStatus(DEPART_FROM_HUB);
                break;
            case OUT_FOR_DELIVERY:
                existingAwb.setAwbStatus(OUT_FOR_DELIVERY);
                break;
            case DELIVERED:
                existingAwb.setAwbStatus(DELIVERED);
                break;
            default:
                throw new RecordNotFoundException("Awb Status not valid");
        }

        Awb updatedAwb = awbRepository.save(existingAwb);
        awbShippingHistoryService.addAwbShippingHistory(updatedAwb);

        sendEmailAsync(updatedAwb);
        return toDto(updatedAwb);
    }

    @Override
    public AwbDto updateAwbStatusAndCommentOnScan(Long uniqueNumber, String awbStatus, String comment) {
        Awb existingAwb = awbRepository.findByUniqueNumber(uniqueNumber)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for Tracking Number => %d", uniqueNumber)));

        User currentUser = helperUtils.getCurrentUser();
        existingAwb.setAssignedToUser(currentUser);
        existingAwb.setCreatedBy(currentUser.getEmail());

        switch (awbStatus) {
            case AWB_CREATED:
                existingAwb.setAwbStatus(AWB_CREATED);
                break;
            case PICKED_UP:
                existingAwb.setAwbStatus(PICKED_UP);
                break;
            case ARRIVED_IN_STATION:
                existingAwb.setAwbStatus(ARRIVED_IN_STATION);
                break;
            case HELD_IN_STATION:
                existingAwb.setAwbStatus(HELD_IN_STATION);
                break;
            case DEPART_FROM_STATION:
                existingAwb.setAwbStatus(DEPART_FROM_STATION);
                break;
            case ARRIVED_IN_HUB:
                existingAwb.setAwbStatus(ARRIVED_IN_HUB);
                break;
            case DEPART_FROM_HUB:
                existingAwb.setAwbStatus(DEPART_FROM_HUB);
                break;
            case OUT_FOR_DELIVERY:
                existingAwb.setAwbStatus(OUT_FOR_DELIVERY);
                break;
            case DELIVERED:
                existingAwb.setAwbStatus(DELIVERED);
                break;
            default:
                throw new RecordNotFoundException("Awb Status not valid");
        }

        Awb updatedAwb = awbRepository.save(existingAwb);
        awbShippingHistoryService.addAwbShippingHistory(updatedAwb);
        awbShippingHistoryService.addCommentToAwbShippingHistory(comment, updatedAwb.getId());

        sendEmailAsync(updatedAwb);
        return toDto(updatedAwb);
    }

    @Override
    public AwbDto updatePdaScanStatusAndCommentOnScan(Long uniqueNumber, String pdaScan, String comment) {
        Awb existingAwb = awbRepository.findByUniqueNumber(uniqueNumber)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for Tracking Number => %d", uniqueNumber)));

        User currentUser = helperUtils.getCurrentUser();
        existingAwb.setAssignedToUser(currentUser);
        existingAwb.setCreatedBy(currentUser.getEmail());
        existingAwb.setPdaScan(pdaScan);

        Awb updatedAwb = awbRepository.save(existingAwb);
        awbShippingHistoryService.addAwbShippingHistoryForMobileApp(updatedAwb);
        awbShippingHistoryService.addCommentToAwbShippingHistory(comment, updatedAwb.getId());

        sendEmailAsync(updatedAwb);
        return toDto(updatedAwb);
    }


    @Override
    @Transactional
    public List<AwbDto> updateMultipleAwbStatusOnScan(Map<Long, String> statusMap) {
        List<Awb> updatedAwbList = new ArrayList<>();

        for (Map.Entry<Long, String> entry : statusMap.entrySet()) {
            Long uniqueNumber = entry.getKey();
            String newStatus = entry.getValue();

            Awb awb = awbRepository.findByUniqueNumber(uniqueNumber)
                    .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for UniqueNumber => %d", uniqueNumber)));

            awb.setAwbStatus(newStatus);

            awbRepository.save(awb);
            updatedAwbList.add(awb);
            awbShippingHistoryService.addAwbShippingHistory(awb);
        }

        return updatedAwbList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Async
    public void sendEmailAsync(Awb createdAwb) {
        try {
            String roleName = createdAwb.getAssignedTo();
            if (roleName != null) {
                List<User> userList = userRepository.findAllByRoleName(roleName);

                for (User user : userList) {
                    assignmentEmailsUtils.sendAssignedAwb(user, createdAwb.getUniqueNumber(), createdAwb.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecordNotFoundException("Error occurred while Sending Email");
        }
    }

    @Override
    public List<AwbDto> getAwbBetweenDates(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        String loggedInUserEmail = getLoggedInUserEmail();
        String loggedInUserRole = getLoggedInUserRole();

        boolean isAdminOrCustomerServiceAgent = "ROLE_ADMIN".equals(loggedInUserRole) || "ROLE_CUSTOMER_SERVICE_AGENT".equals(loggedInUserRole);

        if(isAdminOrCustomerServiceAgent){
            return awbRepository.findAllByCreatedAtBetween(startDateTime, endDateTime)
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }else{
            return awbRepository.findAllByCreatedAtBetweenAndLoggedInUser(startDateTime,endDateTime,loggedInUserEmail,loggedInUserRole)
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());


        }
    }

    @Override
    public List<AwbDto> getAwbListByAwbNumbers(List<Long> awbNumbers) {
        String loggedInUserEmail = getLoggedInUserEmail();
        String loggedInUserRole = getLoggedInUserRole();

        boolean isAdminOrCustomerServiceAgent = "ROLE_ADMIN".equals(loggedInUserRole) || "ROLE_CUSTOMER_SERVICE_AGENT".equals(loggedInUserRole);

        if(isAdminOrCustomerServiceAgent){
            return awbNumbers.stream()
                    .map(awbNumber -> awbRepository.findByUniqueNumber(awbNumber)
                            .orElseThrow(() -> new RecordNotFoundException(
                                    String.format("AWB not found for awbNumber => %d", awbNumber))))
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }else{
            return awbNumbers.stream()
                    .flatMap(awbNumber -> awbRepository.findAllByUniqueNumberAndLoggedInUser(awbNumber, loggedInUserEmail, loggedInUserRole)
                            .stream())
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<AwbDto> getAwbListByAccountNumbers(List<String> awbNumbers) {
        List<AwbDto> awbDtoList = new ArrayList<>();
        Set<String> processedAccountNumbers  = new HashSet<>();

        for (String accountNumber : awbNumbers) {
            // Check if the tracking number has already been processed
            if (processedAccountNumbers .contains(accountNumber)) {
                continue;
            }

            List<Awb> awbs = awbRepository.findByAccountNumber(accountNumber);

            if (awbs != null && !awbs.isEmpty()) {
                for (Awb awb : awbs) {
                    AwbDto awbDto = toDto(awb);
                    awbDtoList.add(awbDto);
                }
                processedAccountNumbers.add(accountNumber);
            }
        }

        if (awbDtoList.isEmpty()) {
            throw new RecordNotFoundException("No AWB found for the provided Account Numbers.");
        }

        return awbDtoList;
    }

    @Override
    public List<AwbDto> getAllAwbListByCreatedBy() {
        String loggedInUserEmail = getLoggedInUserEmail();
        String loggedInUserRole = getLoggedInUserRole();

        boolean isAdminOrCustomerServiceAgent = "ROLE_ADMIN".equals(loggedInUserRole) || "ROLE_CUSTOMER_SERVICE_AGENT".equals(loggedInUserRole);

        if(isAdminOrCustomerServiceAgent){
            return getAll(true);
        }else{
            return awbRepository.findAllByLoggedInUser(loggedInUserEmail,loggedInUserRole)
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Map<String, Long> getAwbStatusCounts() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Add logic to get counts based on different AWB statuses
        statusCounts.put("awbCreated", awbRepository.countByStatusAndAwbStatus(true,"AWB Created"));
        statusCounts.put("picked", awbRepository.countByStatusAndAwbStatus(true,"Picked Up"));
        statusCounts.put("arrivedInStation", awbRepository.countByStatusAndAwbStatus(true,"Arrived in Station"));
        statusCounts.put("heldInStation", awbRepository.countByStatusAndAwbStatus(true,"Held in Station"));
        statusCounts.put("departFromStation", awbRepository.countByStatusAndAwbStatus(true,"Depart from Station"));
        statusCounts.put("arrivedInHub", awbRepository.countByStatusAndAwbStatus(true,"Arrived in Hub"));
        statusCounts.put("departFromHub", awbRepository.countByStatusAndAwbStatus(true,"Depart from Hub"));
        statusCounts.put("outForDelivery", awbRepository.countByStatusAndAwbStatus(true,"Out for Delivery"));
        statusCounts.put("delivered", awbRepository.countByStatusAndAwbStatus(true,"Delivered"));

        return statusCounts;
    }

    @Override
    public Map<String, Long> getStatusCounts() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Add logic to get counts for true status
        statusCounts.put("active", awbRepository.countByStatus(true));

        // Add logic to get counts for false status
        statusCounts.put("inactive", awbRepository.countByStatus(false));

        return statusCounts;
    }

    @Override
    public Map<String, Long> getAwbStatusCountsBasedOnLoggedInUser() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Get the logged-in user's email
        String loggedInUserEmail = getLoggedInUserEmail();
        String loggedInUserRole = getLoggedInUserRole();

        // Add logic to get counts based on different AWB statuses for the logged-in user
        statusCounts.put("awbCreated", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "AWB Created", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("picked", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Picked Up", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("arrivedInStation", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Arrived in Station", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("heldInStation", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Held in Station", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("departFromStation", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Depart from Station", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("arrivedInHub", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Arrived in Hub", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("departFromHub", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Depart from Hub", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("outForDelivery", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Out for Delivery", loggedInUserEmail,loggedInUserRole));
        statusCounts.put("delivered", awbRepository.countByStatusAndAwbStatusAndCreatedByOrAssignedTo(true, "Delivered", loggedInUserEmail,loggedInUserRole));


        return statusCounts;
    }

    @Override
    public Map<String, Long> getStatusCountsBasedOnLoggedInUser() {
        Map<String, Long> statusCounts = new HashMap<>();

        // Get the logged-in user's email
        String loggedInUserEmail = getLoggedInUserEmail();
        String loggedInUserRole = getLoggedInUserRole();

        // Add logic to get counts based on different AWB statuses for the logged-in user
        statusCounts.put("active", awbRepository.countByStatusAndCreatedByOrAssignedTo(true,  loggedInUserEmail,loggedInUserRole));
        statusCounts.put("inactive", awbRepository.countByStatusAndCreatedByOrAssignedTo(false,  loggedInUserEmail,loggedInUserRole));

        return statusCounts;
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
    public LocalDate getMinCreatedAt() {
        return awbRepository.findMinCreatedAt();
    }

    @Override
    public LocalDate getMaxCreatedAt() {
        return awbRepository.findMaxCreatedAt();
    }

    @Override
    public Long getAllAwbByAssignedUser() {
        User user = helperUtils.getCurrentUser();
        return awbRepository.countByLoggedInUser(user.getName());
    }

    @Override
    @Transactional
    public AwbDto assignAwbToUser(Long userId, Long awbId) {
        Awb awb = awbRepository.findById(awbId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", awbId)));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User not found for id => %d", userId)));

        awb.setAssignedToUser(user);
        Awb assignedAwb = awbRepository.save(awb);
        return toDto(assignedAwb);
    }

    @Override
    public List<Map<String,Object>> getAwbByStatusChangedOnPreviousDay(String status) {
        // Calculate the start and end times for the previous day
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        List<AwbShippingHistory> awbList = awbShippingHistoryRepository.findByAwbStatusAndTimestampBetween(status, startDateTime, endDateTime);

        List<Map<String, Object>> result = new ArrayList<>();
        int count = 1;

        for (AwbShippingHistory awbShippingHistory : awbList) {

            User user = awbShippingHistory.getStatusUpdateByUser();
            if(user == null){
                throw new RecordNotFoundException("User not found");
            }
            String location = user.getCity() + ", " + user.getCountry();

            Awb awb = awbRepository.findById(awbShippingHistory.getAwb().getId())
                    .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", awbShippingHistory.getAwb().getId())));

            Map<String, Object> awbMap = new LinkedHashMap<>();
            awbMap.put("S.#", count++);
            awbMap.put("AWB#", awb.getUniqueNumber());
            awbMap.put("Last Status", awbShippingHistory.getAwbStatus());
            awbMap.put("Location", location);
            awbMap.put("Date", awbShippingHistory.getTimestamp().toLocalDate());
            awbMap.put("ID", awbShippingHistory.getId());
            awbMap.put("Comments", awbShippingHistory.getComment());

            result.add(awbMap);
        }

        return result;
    }

    @Override
    public List<Map<String,Object>> getAwbByStatusChangedLastDayExcludingPickedUpAndDelivered() {
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

        List<AwbShippingHistory> awbList = awbShippingHistoryRepository.findByTimestampBetweenAndAwbStatusNotIn(startDateTime, endDateTime,
                Arrays.asList(PICKED_UP, DELIVERED));

        List<Map<String, Object>> result = new ArrayList<>();
        int count = 1;

        for (AwbShippingHistory awbShippingHistory : awbList) {

            User user = awbShippingHistory.getStatusUpdateByUser();
            if(user == null){
                throw new RecordNotFoundException("User not found");
            }
            String location = user.getCity() + ", " + user.getCountry();

            Awb awb = awbRepository.findById(awbShippingHistory.getAwb().getId())
                    .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", awbShippingHistory.getAwb().getId())));

            Map<String, Object> awbMap = new LinkedHashMap<>();
            awbMap.put("S.#", count++);
            awbMap.put("AWB#", awb.getUniqueNumber());
            awbMap.put("Last Status", awbShippingHistory.getAwbStatus());
            awbMap.put("Location", location);
            awbMap.put("Date", awbShippingHistory.getTimestamp().toLocalDate());
            awbMap.put("ID", awbShippingHistory.getId());
            awbMap.put("Comments", awbShippingHistory.getComment());

            result.add(awbMap);
        }

        return result;
    }

    @Override
    public List<AwbDto> findAwbByTrackingNumbers(List<Long> trackingNumbers) {
        List<AwbDto> awbDtoList = new ArrayList<>();
        Set<Long> processedTrackingNumbers = new HashSet<>();

        for (Long trackingNumber : trackingNumbers) {
            // Check if the tracking number has already been processed
            if (processedTrackingNumbers.contains(trackingNumber)) {
                continue;
            }

            Awb awb = awbRepository.findByTrackingNumber(trackingNumber);

            if (awb != null) {
                AwbDto awbDto = toDto(awb);
                awbDtoList.add(awbDto);
                processedTrackingNumbers.add(trackingNumber);
            }
        }

        return awbDtoList;
    }

    public AwbDto toDto(Awb awb) {
        return AwbDto.builder()
                .id(awb.getId())
                .uniqueNumber(awb.getUniqueNumber())
                .createdAt(awb.getCreatedAt())
                .shipperName(awb.getShipperName())
                .shipperContactNumber(awb.getShipperContactNumber())
                .originCountry(awb.getOriginCountry())
                .originCity(awb.getOriginCity())
                .pickupAddress(awb.getPickupAddress())
                .shipperRefNumber(awb.getShipperRefNumber())
                .recipientsName(awb.getRecipientsName())
                .recipientsContactNumber(awb.getRecipientsContactNumber())
                .destinationCountry(awb.getDestinationCountry())
                .destinationCity(awb.getDestinationCity())
                .deliveryAddress(awb.getDeliveryAddress())
                .pickupDate(awb.getPickupDate())
                .pickupTime(awb.getPickupTime())
                .productType(awb.getProductType())
                .serviceType(awb.getServiceType())
                .requestType(awb.getRequestType())
                .pieces(awb.getPieces())
                .content(awb.getContent())
                .weight(awb.getWeight())
                .amount(awb.getAmount())
                .currency(awb.getCurrency())
                .dutyAndTaxesBillTo(awb.getDutyAndTaxesBillTo())
                .status(awb.getStatus())
                .emailFlag(awb.getEmailFlag())
                .awbUrl(awb.getAwbUrl())
                .awbStatus(awb.getAwbStatus())
                .accountNumber(awb.getAccountNumber())
                .serviceTypeCode(awb.getServiceTypeCode())
                .pickupDistrict(awb.getPickupDistrict())
                .pickupStreetName(awb.getPickupStreetName())
                .deliveryDistrict(awb.getDeliveryDistrict())
                .deliveryStreetName(awb.getDeliveryStreetName())
                .createdBy(awb.getCreatedBy())
                .assignedTo(awb.getAssignedTo())
                .assignedToUser(awb.getAssignedToUser())
                .pdaScan(awb.getPdaScan())
                .build();
    }

    public Awb toEntity(AwbDto awbDto) {
        return Awb.builder()
                .id(awbDto.getId())
                .uniqueNumber(awbDto.getUniqueNumber())
                .createdAt(awbDto.getCreatedAt())
                .shipperName(awbDto.getShipperName())
                .shipperContactNumber(awbDto.getShipperContactNumber())
                .originCountry(awbDto.getOriginCountry())
                .originCity(awbDto.getOriginCity())
                .pickupAddress(awbDto.getPickupAddress())
                .shipperRefNumber(awbDto.getShipperRefNumber())
                .recipientsName(awbDto.getRecipientsName())
                .recipientsContactNumber(awbDto.getRecipientsContactNumber())
                .destinationCountry(awbDto.getDestinationCountry())
                .destinationCity(awbDto.getDestinationCity())
                .deliveryAddress(awbDto.getDeliveryAddress())
                .pickupDate(awbDto.getPickupDate())
                .pickupTime(awbDto.getPickupTime())
                .requestType(awbDto.getRequestType())
                .productType(awbDto.getProductType())
                .serviceType(awbDto.getServiceType())
                .pieces(awbDto.getPieces())
                .content(awbDto.getContent())
                .weight(awbDto.getWeight())
                .amount(awbDto.getAmount())
                .currency(awbDto.getCurrency())
                .dutyAndTaxesBillTo(awbDto.getDutyAndTaxesBillTo())
                .status(awbDto.getStatus())
                .emailFlag(awbDto.getEmailFlag())
                .awbUrl(awbDto.getAwbUrl())
                .awbStatus(awbDto.getAwbStatus())
                .accountNumber(awbDto.getAccountNumber())
                .serviceTypeCode(awbDto.getServiceTypeCode())
                .pickupDistrict(awbDto.getPickupDistrict())
                .pickupStreetName(awbDto.getPickupStreetName())
                .deliveryDistrict(awbDto.getDeliveryDistrict())
                .deliveryStreetName(awbDto.getDeliveryStreetName())
                .createdBy(awbDto.getCreatedBy())
                .assignedTo(awbDto.getAssignedTo())
                .assignedToUser(awbDto.getAssignedToUser())
                .pdaScan(awbDto.getPdaScan())
                .build();
    }
}
