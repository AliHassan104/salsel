package com.salsel.service.impl;

import com.salsel.dto.AccountDto;
import com.salsel.dto.BillingDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Account;
import com.salsel.model.Billing;
import com.salsel.repository.BillingRepository;
import com.salsel.service.BillingService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

//    @Override
//    public Map<Long, List<BillingDto>> getAllGroupedByInvoice(Boolean status) {
//        List<Billing> billings = billingRepository.findAllInDesOrderByIdAndStatus(status);
//
//        Map<Long, List<BillingDto>> result = billings.stream()
//                .collect(Collectors.groupingBy(Billing::getInvoiceNo,
//                        Collectors.mapping(this::toDto, Collectors.toList())));
//
//        return result;
//    }


    // Check if a row is empty (all cells are empty)
    private boolean isRowEmpty(Row row) {
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            if (row.getCell(i) != null && !row.getCell(i).toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
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
    @Transactional
    public void deleteById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Account not found for id => %d", id)));
        billingRepository.setStatusInactive(billing.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Account not found for id => %d", id)));
        billingRepository.setStatusActive(billing.getId());
    }

    @Override
    public List<Map<String, Object>> getBillingInvoiceDataByExcel() {
        List<Billing> billingList = billingRepository.getAllBillingsWhereStatusIsNotClosed();
        List<Map<String, Object>> result = new ArrayList<>();
        int count = 1;
        double totalCharges = 0.0;

        for (Billing billing : billingList) {
            Map<String, Object> billingMap = new LinkedHashMap<>();
            billingMap.put("#", count++);
            billingMap.put("Airway No", billing.getAirwayBillNo());
            billingMap.put("Customer Ref#", billing.getCustomerRef());
            billingMap.put("Product", billing.getProduct());
            billingMap.put("Service Details", billing.getServiceDetails());
            billingMap.put("Charges", billing.getCharges());
            billingMap.put("Customer Account",billing.getCustomerAccountNumber());
            billingMap.put("Invoice No",billing.getInvoiceNo());
            billingMap.put("Invoice Date",billing.getInvoiceDate());

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
                .invoiceNo(billing.getInvoiceNo())
                .taxInvoiceTo(billing.getTaxInvoiceTo())
                .invoiceDate(billing.getInvoiceDate())
                .city(billing.getCity())
                .country(billing.getCountry())
                .taxNo(billing.getTaxNo())
                .address(billing.getAddress())
                .invoiceType(billing.getInvoiceType())
                .customerRef(billing.getCustomerRef())
                .airwayBillNo(billing.getAirwayBillNo())
                .customerAccountNumber(billing.getCustomerAccountNumber())
                .product(billing.getProduct())
                .status(billing.getStatus())
                .billingStatus(billing.getBillingStatus())
                .vatTax(billing.getVatTax())
                .build();
    }

    public Billing toEntity(BillingDto billingDto){
        return Billing.builder()
                .id(billingDto.getId())
                .serviceDetails(billingDto.getServiceDetails())
                .charges(billingDto.getCharges())
                .invoiceNo(billingDto.getInvoiceNo())
                .taxInvoiceTo(billingDto.getTaxInvoiceTo())
                .invoiceDate(billingDto.getInvoiceDate())
                .city(billingDto.getCity())
                .country(billingDto.getCountry())
                .taxNo(billingDto.getTaxNo())
                .address(billingDto.getAddress())
                .invoiceType(billingDto.getInvoiceType())
                .customerRef(billingDto.getCustomerRef())
                .airwayBillNo(billingDto.getAirwayBillNo())
                .customerAccountNumber(billingDto.getCustomerAccountNumber())
                .product(billingDto.getProduct())
                .status(billingDto.getStatus())
                .billingStatus(billingDto.getBillingStatus())
                .vatTax(billingDto.getVatTax())
                .build();
    }
}
