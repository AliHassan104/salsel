package com.salsel.service.impl;

import com.salsel.dto.AwbDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.repository.AwbRepository;
import com.salsel.service.AwbService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwbServiceImpl implements AwbService {
    private final AwbRepository awbRepository;

    public AwbServiceImpl(AwbRepository awbRepository) {
        this.awbRepository = awbRepository;
    }

    @Override
    @Transactional
    public AwbDto save(AwbDto awbDto) {
        Awb awb = toEntity(awbDto);
        awb.setStatus(true);

        Awb createdAwb = awbRepository.save(awb);
        return toDto(createdAwb);
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
                .build();
    }

    public Awb toEntity(AwbDto awbDto) {
        return Awb.builder()
                .id(awbDto.getId())
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
                .build();
    }
}
