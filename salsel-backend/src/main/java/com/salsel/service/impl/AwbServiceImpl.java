package com.salsel.service.impl;

import com.salsel.dto.AwbDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.repository.AwbRepository;
import com.salsel.service.AwbService;
import com.salsel.service.BucketService;
import com.salsel.service.CodeGenerationService;
import com.salsel.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwbServiceImpl implements AwbService {

    @Value("${spring.mail.username}")
    private String sender;
    private final AwbRepository awbRepository;
    private final CodeGenerationService codeGenerationService;
    private final PdfGenerationService pdfGenerationService;
    private final BucketService bucketService;

    public AwbServiceImpl(AwbRepository awbRepository, CodeGenerationService codeGenerationService, PdfGenerationService pdfGenerationService, BucketService bucketService) {
        this.awbRepository = awbRepository;
        this.codeGenerationService = codeGenerationService;
        this.pdfGenerationService = pdfGenerationService;
        this.bucketService = bucketService;
    }

    @Override
    @Transactional
    public AwbDto save(AwbDto awbDto) {
        try {
            Long maxUniqueNumber = awbRepository.findMaxUniqueNumber();
            awbDto.setUniqueNumber(maxUniqueNumber == null ? 900000001L : maxUniqueNumber + 1);

            Awb awb = toEntity(awbDto);
            awb.setStatus(true);
            awb.setEmailFlag(false);
            Awb createdAwb = awbRepository.save(awb);
            Long awbId = createdAwb.getId();

            codeGenerationService.generateBarcode(awb.getUniqueNumber().toString(), awbId);
            codeGenerationService.generateBarcodeVertical(awb.getUniqueNumber().toString(), awbId);
            codeGenerationService.generateQRCode("https://example.com", awbId);

            return toDto(createdAwb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecordNotFoundException("Error occurred while processing the request");
        }
    }


    @Override
    public List<AwbDto> getAll() {
        List<Awb> awbList = awbRepository.findAllInDesOrderByIdAndStatus();
        List<AwbDto> awbDtoList = new ArrayList<>();

        for (Awb awb : awbList) {
            AwbDto awbDto = toDto(awb);
            awbDtoList.add(awbDto);
        }
        return awbDtoList;
    }

    @Override
    public byte[] downloadAwbPdf(String fileName, Long awbId) {
        Model model = new ExtendedModelMap();
        model.addAttribute("awbId", awbId);
        return pdfGenerationService.generatePdf("Awb", model, awbId);
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
        existingAwb.setDutyAndTaxesBillTo(awbDto.getDutyAndTaxesBillTo());

        Awb updatedAwb = awbRepository.save(existingAwb);
        return toDto(updatedAwb);
    }

    public AwbDto toDto(Awb awb) {
        return AwbDto.builder()
                .id(awb.getId())
                .uniqueNumber(awb.getUniqueNumber())
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
                .pieces(awb.getPieces())
                .content(awb.getContent())
                .weight(awb.getWeight())
                .amount(awb.getAmount())
                .currency(awb.getCurrency())
                .dutyAndTaxesBillTo(awb.getDutyAndTaxesBillTo())
                .status(awb.getStatus())
                .emailFlag(awb.getEmailFlag())
                .awbUrl(awb.getAwbUrl())
                .build();
    }

    public Awb toEntity(AwbDto awbDto) {
        return Awb.builder()
                .id(awbDto.getId())
                .uniqueNumber(awbDto.getUniqueNumber())
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
                .build();
    }
}
