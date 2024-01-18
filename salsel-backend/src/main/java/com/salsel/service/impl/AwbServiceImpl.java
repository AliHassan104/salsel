package com.salsel.service.impl;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.salsel.dto.AwbDto;
import com.salsel.dto.CustomUserDetail;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.model.Role;
import com.salsel.model.User;
import com.salsel.repository.AwbRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.AwbService;
import com.salsel.service.CodeGenerationService;
import com.salsel.service.PdfGenerationService;
import com.salsel.utils.AssignmentEmailsUtils;
import com.salsel.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwbServiceImpl implements AwbService {

    @Value("${spring.mail.username}")
    private String sender;
    private final AwbRepository awbRepository;
    private final UserRepository userRepository;
    private final CodeGenerationService codeGenerationService;
    private final AssignmentEmailsUtils assignmentEmailsUtils;

    private final PdfGenerationService pdfGenerationService;

    public AwbServiceImpl(AwbRepository awbRepository, UserRepository userRepository, CodeGenerationService codeGenerationService, AssignmentEmailsUtils assignmentEmailsUtils, PdfGenerationService pdfGenerationService) {
        this.awbRepository = awbRepository;
        this.userRepository = userRepository;
        this.codeGenerationService = codeGenerationService;
        this.assignmentEmailsUtils = assignmentEmailsUtils;
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
            Awb createdAwb = awbRepository.save(awb);
            Long awbId = createdAwb.getId();

            codeGenerationService.generateBarcode(awb.getUniqueNumber().toString(), awbId);
            codeGenerationService.generateBarcodeVertical(awb.getUniqueNumber().toString(), awbId);
            codeGenerationService.generateQRCode(awb.getUniqueNumber().toString(), awbId);

            String roleName = createdAwb.getAssignedTo();
            if(roleName != null){
                List<User> userList = userRepository.findAllByRoleName(roleName);

                for(User user : userList){
                    assignmentEmailsUtils.sendAssignedAwb(user,createdAwb.getUniqueNumber(),createdAwb.getId());
                }
            }

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

        existingAwb.setShipperName(awbDto.getShipperName());
        existingAwb.setShipperContactNumber(awbDto.getShipperContactNumber());
        existingAwb.setOriginCountry(awbDto.getOriginCountry());
        existingAwb.setOriginCity(awbDto.getOriginCity());
        existingAwb.setPickupAddress(awbDto.getPickupAddress());
        existingAwb.setShipperRefNumber(awbDto.getShipperRefNumber());
        existingAwb.setRecipientsName(awbDto.getRecipientsName());
        existingAwb.setRecipientsContactNumber(awbDto.getRecipientsContactNumber());
        existingAwb.setDestinationCountry(awbDto.getDestinationCountry());
        existingAwb.setDestinationCity(awbDto.getDestinationCity());
        existingAwb.setDeliveryAddress(awbDto.getDeliveryAddress());
        existingAwb.setPickupDate(awbDto.getPickupDate());
        existingAwb.setPickupTime(awbDto.getPickupTime());
        existingAwb.setProductType(awbDto.getProductType());
        existingAwb.setServiceType(awbDto.getServiceType());
        existingAwb.setPieces(awbDto.getPieces());
        existingAwb.setContent(awbDto.getContent());
        existingAwb.setWeight(awbDto.getWeight());
        existingAwb.setAmount(awbDto.getAmount());
        existingAwb.setCurrency(awbDto.getCurrency());
        existingAwb.setRequestType(awbDto.getRequestType());
        existingAwb.setDutyAndTaxesBillTo(awbDto.getDutyAndTaxesBillTo());
        existingAwb.setAwbStatus(awbDto.getAwbStatus());
        existingAwb.setAccountNumber(awbDto.getAccountNumber());
        existingAwb.setServiceTypeCode(awbDto.getServiceTypeCode());
        existingAwb.setDeliveryDistrict(awbDto.getDeliveryDistrict());
        existingAwb.setDeliveryStreetName(awbDto.getDeliveryStreetName());
        existingAwb.setPickupStreetName(awbDto.getPickupStreetName());
        existingAwb.setPickupDistrict(awbDto.getPickupDistrict());
        existingAwb.setAssignedTo(awbDto.getAssignedTo());

        Awb updatedAwb = awbRepository.save(existingAwb);
        return toDto(updatedAwb);
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
                .build();
    }
}
