package com.salsel.service.impl;

import com.salsel.dto.AccountDto;
import com.salsel.dto.BillingDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Billing;
import com.salsel.repository.BillingRepository;
import com.salsel.service.BillingService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;

    public BillingServiceImpl(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @Override
    @Transactional
    public BillingDto save(BillingDto billingDto) {
        Billing billing = toEntity(billingDto);
        billing.setStatus(true);
        Billing createdBilling = billingRepository.save(billing);

        return toDto(createdBilling);
    }

    @Override
    public List<BillingDto> getAll(Boolean status) {
        List<Billing> billings = billingRepository.findAllInDesOrderByIdAndStatus(status);
        List<BillingDto> billingDtoList = new ArrayList<>();

        for(Billing billing: billings){
            BillingDto billingDto = toDto(billing);
            billingDtoList.add(billingDto);
        }

        return billingDtoList;
    }

    @Override
    public List<BillingDto> getAllBillingsWhereStatusIsNotClosed() {
        List<Billing> billings = billingRepository.getAllBillingsWhereStatusIsNotClosed();
        List<BillingDto> billingDtoList = new ArrayList<>();

        for(Billing billing: billings){
            BillingDto billingDto = toDto(billing);
            billingDtoList.add(billingDto);
        }

        return billingDtoList;
    }

    @Override
    public BillingDto findById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Bill not found for id ==> %d", id)));
                return toDto(billing);
    }

    @Override
    public List<Map<String, Object>> getBillingByExcel() {
        List<Billing> billingList = billingRepository.getAllBillingsWhereStatusIsNotClosed();
        List<Map<String, Object>> result = new ArrayList<>();
        int count = 1;
        double totalCharges = 0.0;

        for (Billing billing : billingList) {
            Map<String, Object> billingMap = new LinkedHashMap<>();
            billingMap.put("#", count++);
            billingMap.put("Account Number", billing.getAccountNumber());
            billingMap.put("Shipment Number", billing.getShipmentNumber());
            billingMap.put("Product", billing.getProduct());
            billingMap.put("Service Details", billing.getServiceDetails());
            billingMap.put("Charges", billing.getCharges());

            totalCharges += billing.getCharges(); // Calculate total charges

            result.add(billingMap);
        }

        // Add total charges row
        Map<String, Object> totalRow = new LinkedHashMap<>();
        totalRow.put("#", "Total Charges");
        totalRow.put("Account Number", "");
        totalRow.put("Shipment Number", "");
        totalRow.put("Product", "");
        totalRow.put("Service Details", "");
        totalRow.put("Charges", totalCharges);
        result.add(totalRow);

        return result;
    }


    public BillingDto toDto(Billing billing){
        return BillingDto.builder()
                .id(billing.getId())
                .serviceDetails(billing.getServiceDetails())
                .charges(billing.getCharges())
                .shipmentNumber(billing.getShipmentNumber())
                .accountNumber(billing.getAccountNumber())
                .product(billing.getProduct())
                .status(billing.getStatus())
                .billingStatus(billing.getBillingStatus())
                .build();
    }

    public Billing toEntity(BillingDto billingDto){
        return Billing.builder()
                .id(billingDto.getId())
                .serviceDetails(billingDto.getServiceDetails())
                .charges(billingDto.getCharges())
                .shipmentNumber(billingDto.getShipmentNumber())
                .accountNumber(billingDto.getAccountNumber())
                .product(billingDto.getProduct())
                .status(billingDto.getStatus())
                .billingStatus(billingDto.getBillingStatus())
                .build();
    }
}
