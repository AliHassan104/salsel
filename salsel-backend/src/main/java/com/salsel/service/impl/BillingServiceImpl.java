package com.salsel.service.impl;

import com.amazonaws.services.cloudwatch.model.InvalidFormatException;
import com.salsel.dto.BillingDto;
import com.salsel.exception.BillingException;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Account;
import com.salsel.model.Awb;
import com.salsel.model.Billing;
import com.salsel.model.BillingAttachment;
import com.salsel.repository.AccountRepository;
import com.salsel.repository.AwbRepository;
import com.salsel.repository.BillingAttachmentRepository;
import com.salsel.repository.BillingRepository;
import com.salsel.service.BillingService;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.PdfGenerationService;
import com.salsel.utils.EmailUtils;
import com.salsel.utils.ExcelUtils;
import com.salsel.utils.HelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.salsel.constants.BillingConstants.*;
import static com.salsel.service.impl.bucketServiceImpl.INVOICE;

@Service
@Slf4j
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;
    private final AccountRepository accountRepository;
    private final AwbRepository awbRepository;
    private final BillingAttachmentRepository billingAttachmentRepository;
    private final HelperUtils helperUtils;
    private final EmailUtils emailUtils;
    private final PdfGenerationService pdfGenerationService;
    private final ExcelGenerationService excelGenerationService;

    public BillingServiceImpl(BillingRepository billingRepository, AccountRepository accountRepository, AwbRepository awbRepository, BillingAttachmentRepository billingAttachmentRepository, HelperUtils helperUtils, EmailUtils emailUtils, PdfGenerationService pdfGenerationService, ExcelGenerationService excelGenerationService) {
        this.billingRepository = billingRepository;
        this.accountRepository = accountRepository;
        this.awbRepository = awbRepository;
        this.billingAttachmentRepository = billingAttachmentRepository;
        this.helperUtils = helperUtils;
        this.emailUtils = emailUtils;
        this.pdfGenerationService = pdfGenerationService;
        this.excelGenerationService = excelGenerationService;
    }

    @Override
    @Transactional
    public List<BillingAttachment> save(MultipartFile file) {

        List<BillingDto> billingDtoList = uploadDataExcel(file);
        List<Map<String, Object>> invoices = getBillingInvoiceDataByExcelUploaded(billingDtoList);
        Map<Long, List<byte[]>> invoicesPdf = pdfGenerationService.generateBillingPdf(invoices);
        Map<Long, List<ByteArrayOutputStream>> excelFiles = excelGenerationService.generateBillingReports(invoices);

        List<Billing> billingEntities = new ArrayList<>();
        for (Map<String, Object> bill : invoices) {
            Account account = accountRepository.findByAccountNumber((Long) bill.get(ACCOUNT_NUMBER));
            if (account == null) {
                throw new RecordNotFoundException("Account not found at this account number: " + (Long) bill.get(ACCOUNT_NUMBER));
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> invoiceDetails = (List<Map<String, Object>>) bill.get(INVOICES);
            for (Map<String, Object> invoiceDetail : invoiceDetails) {
                Billing billing = Billing.builder()
                        .airwayBillNo((Long) invoiceDetail.get(AIRWAY_NO))
                        .customerAccountNumber((Long) bill.get(ACCOUNT_NUMBER))
                        .customerRef((String) invoiceDetail.get(CUSTOMER_REF))
                        .address((String) bill.get(ADDRESS))
                        .invoiceDate((LocalDate) bill.get(INVOICE_DATE))
                        .invoiceNo((String) bill.get(INVOICE_NO))
                        .product(invoiceDetail.get(PRODUCT).toString())
                        .serviceDetails(invoiceDetail.get(SERVICE_DETAILS).toString())
                        .charges((Double) invoiceDetail.get(CHARGES))
                        .taxNo((String) bill.get(TAX_NO))
                        .taxInvoiceTo((String) bill.get(TAX_INVOICE_NO))
                        .city(account.getCity())
                        .country(account.getCounty())
                        .status(true)
                        .isEmailSend(false)
                        .build();

                billingEntities.add(billing);
            }
        }

        List<Billing> savedBillingList = billingRepository.saveAll(billingEntities);

        List<BillingAttachment> billingAttachmentList = new ArrayList<>();
        if (invoicesPdf != null && !invoicesPdf.isEmpty()) {
            for (Map.Entry<Long, List<byte[]>> pdfEntry : invoicesPdf.entrySet()) {
                Long accountNumber = pdfEntry.getKey();
                List<byte[]> pdfs = pdfEntry.getValue();
                String folderName = "Invoice_" + accountNumber;

                List<String> savedPdfUrls = helperUtils.saveInvoicePdfListToS3(pdfs, folderName, INVOICE);
                List<String> savedExcelUrls = new ArrayList<>();

                // Process the corresponding Excel files for the same account number
                if (excelFiles.containsKey(accountNumber)) {
                    List<ByteArrayOutputStream> excelStreams = excelFiles.get(accountNumber);
                    List<byte[]> excelBytesList = new ArrayList<>();
                    for (ByteArrayOutputStream excelStream : excelStreams) {
                        excelBytesList.add(excelStream.toByteArray());
                    }
                    savedExcelUrls = helperUtils.saveInvoiceExcelListToS3(excelBytesList, folderName, INVOICE);
                }

                // Create BillingAttachment objects for each PDF and Excel file
                for (int i = 0; i < savedPdfUrls.size(); i++) {
                    String savedPdfUrl = savedPdfUrls.get(i);
                    BillingAttachment billingAttachment = new BillingAttachment();
                    billingAttachment.setAccountNumber(accountNumber);
                    billingAttachment.setPdfUrl(savedPdfUrl);
                    billingAttachment.setExcelUrl(i < savedExcelUrls.size() ? savedExcelUrls.get(i) : null);
                    billingAttachmentList.add(billingAttachment);
                    billingAttachmentRepository.save(billingAttachment);
                }

                log.info("Invoices are uploaded to S3 in folder '{}'.", folderName);
            }
        }
        return billingAttachmentList;
    }

    @Override
    @Transactional
    public List<BillingDto> uploadDataExcel(MultipartFile file) {
        try {
            if (!ExcelUtils.isExcelFile(file)) {
                throw new RecordNotFoundException("Please Enter a valid excel file.");
            }

            boolean headersMatch = ExcelUtils.validateExcelHeaders(file);
            if (!headersMatch) {
                throw new RecordNotFoundException("Excel Headers don't match.");
            }
            log.info("Headers Matched");

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw new BillingException("Excel Validation Failed");
        }

        List<Billing> billingList = ExcelUtils.processExcelFile(file);
        if (!billingList.isEmpty()) {
            return billingList.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } else {
            throw new RecordNotFoundException("Excel File is empty.");
        }
    }

    @Override
    public List<BillingDto> getAll(Boolean status) {
        List<Billing> billings = billingRepository.findAllInDesOrderByIdAndStatus(status);
        List<BillingDto> billingDtoList = new ArrayList<>();

        for (Billing billing : billings) {
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

    @Override
    public List<BillingDto> getAllBillingsWhereStatusIsNotClosed() {
        List<Billing> billings = billingRepository.getAllBillingsWhereStatusIsNotClosed();
        List<BillingDto> billingDtoList = new ArrayList<>();

        for (Billing billing : billings) {
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
                .orElseThrow(() -> new RecordNotFoundException(String.format("Billing not found for id => %d", id)));
        billingRepository.setStatusInactive(billing.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Billing not found for id => %d", id)));
        billingRepository.setStatusActive(billing.getId());
    }

    @Override
    public void resendBillingInvoice(Long billingId) {
        Billing billing = billingRepository.findById(billingId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Billing not found for id => %d", billingId)));

        BillingAttachment billingAttachment = billingAttachmentRepository.findFirstByAccountNumberOrderByCreatedAtDesc(billing.getCustomerAccountNumber())
                .orElseThrow(() -> new RecordNotFoundException(String.format("BillingAttachments not found for account number => %d", billing.getCustomerAccountNumber())));

        Account account = accountRepository.findByAccountNumber(billing.getCustomerAccountNumber());
        if (account != null) {
            String email = account.getEmail();
            if (email == null) {
                throw new RecordNotFoundException(String.format("Email not found in Account for accountNumber: %d", billing.getCustomerAccountNumber()));
            }

            String pdfUrl = billingAttachment.getPdfUrl();
            String excelUrl = billingAttachment.getExcelUrl();
            if (pdfUrl == null || excelUrl == null) {
                throw new RecordNotFoundException(String.format("PDF or Excel URL is null for account: %d", billing.getCustomerAccountNumber()));
            }

            byte[] pdfContent = helperUtils.downloadAttachment(pdfUrl);
            byte[] excelContent = helperUtils.downloadAttachment(excelUrl);
            emailUtils.sendBillingEmailWithAttachments(email, pdfContent, excelContent);
            helperUtils.updateBillingStatus(billing.getCustomerAccountNumber());
            log.info("Email sent with both excel and pdf to address: {}", email);
        } else {
            throw new RecordNotFoundException(String.format("Account not found for accountNumber: %d", billing.getCustomerAccountNumber()));
        }
    }

    @Override
    public List<Map<String, Object>> getSalaasilStatmentForAllTheInvoices() {
        LocalDate date = LocalDate.now();
        List<Billing> billings = billingRepository.findAllBillingsUpToDate(date);
        List<Map<String, Object>> result = new ArrayList<>();
        int count = 0;

        Set<String> processedInvoices = new HashSet<>();

        for (Billing billing : billings) {
            String invoiceNo = billing.getInvoiceNo();
            if (processedInvoices.contains(invoiceNo)) {
                continue; // Skip already processed invoices
            }

            List<Billing> sameInvoiceBillings = billings.stream()
                    .filter(b -> b.getInvoiceNo().equals(invoiceNo))
                    .collect(Collectors.toList());

            double totalAmountBeforeTax = sameInvoiceBillings.stream()
                    .mapToDouble(Billing::getCharges)
                    .sum();

            // Calculate VAT/tax (assuming 5%)
            double vat = totalAmountBeforeTax * 0.05;
            double totalAmount = totalAmountBeforeTax + vat;
            count++;

            Map<String, Object> billingMap = new LinkedHashMap<>();
            billingMap.put(SNo, count);
            billingMap.put(ACCOUNT_NUMBER, billing.getCustomerAccountNumber());
            billingMap.put(INVOICE_NO, invoiceNo);
            billingMap.put(CUSTOMER_NAME, billing.getTaxInvoiceTo());
            billingMap.put(INVOICE_DATE, billing.getInvoiceDate());
            billingMap.put(INVOICE_TYPE, "Standard");
            billingMap.put(CUSTOMER_REF, billing.getCustomerRef());
            billingMap.put(NET, totalAmount);
            billingMap.put(STATUS, billing.getBillingStatus());

            result.add(billingMap);
            processedInvoices.add(invoiceNo);
        }

        return result;
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
            billingMap.put("Customer Account", billing.getCustomerAccountNumber());
            billingMap.put("Invoice No", billing.getInvoiceNo());
            billingMap.put("Invoice Date", billing.getInvoiceDate());

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

    @Override
    public List<Map<String, Object>> getBillingInvoiceDataByExcelUploaded(List<BillingDto> billingDtoList) {
        String invoiceNo = null;
        List<Map<String, Object>> result = new ArrayList<>();
        Set<Long> processedAirwayBillNos = new HashSet<>();

        // Extract unique account numbers
        Set<Long> accountNumbers = billingDtoList.stream()
                .map(BillingDto::getCustomerAccountNumber)
                .collect(Collectors.toSet());

        // Fetch the latest invoice number only once
        Optional<Billing> latestBilling = billingRepository.findBillingByLatestId();
        if (latestBilling.isPresent()) {
            Billing existingBilling = latestBilling.get();
            String currentInvoiceNo = existingBilling.getInvoiceNo();
            Long existingInvoiceNo = Long.parseLong(currentInvoiceNo);

            // Set the initial invoice number, maintaining leading zeros if present
            existingInvoiceNo++;
            if (currentInvoiceNo.matches("^0\\d+$")) {
                invoiceNo = String.format("%0" + currentInvoiceNo.length() + "d", existingInvoiceNo);
            } else {
                invoiceNo = String.valueOf(existingInvoiceNo);
            }
        } else {
            invoiceNo = "0001";
        }

        // Iterate over each account number
        for (Long accountNumber : accountNumbers) {
            Account account = accountRepository.findByAccountNumber(accountNumber);
            if (account == null) {
                throw new RecordNotFoundException("Account not found for this accountNumber: " + accountNumber);
            }

            List<Map<String, Object>> accountInvoices = new ArrayList<>();
            double totalCharges = 0.0;
            LocalDate invoiceDate = LocalDate.now(); // Assuming today's date for the example

            // Filter and map the billing DTOs for the current account number
            for (BillingDto billingDto : billingDtoList) {
                if (accountNumber.equals(billingDto.getCustomerAccountNumber())) {
                    Map<String, Object> billingMap = new LinkedHashMap<>();
                    Long airwayBillNo = billingDto.getAirwayBillNo();

                    if (airwayBillNo != null && !processedAirwayBillNos.add(airwayBillNo)) {
                        throw new RecordNotFoundException("Duplicate airway bill number found: " + airwayBillNo);
                    }

                    Awb awb = awbRepository.findByUniqueNumber(billingDto.getAirwayBillNo())
                            .orElseThrow(() -> new RecordNotFoundException("Awb not exist in the system: " + billingDto.getAirwayBillNo()));

                    billingMap.put(AIRWAY_NO, billingDto.getAirwayBillNo());
                    billingMap.put(CUSTOMER_REF, billingDto.getCustomerRef());
                    billingMap.put(PRODUCT, billingDto.getProduct());
                    billingMap.put(SERVICE_DETAILS, billingDto.getServiceDetails());
                    billingMap.put(CHARGES, billingDto.getCharges());
                    billingMap.put(ACCOUNT_NUMBER, accountNumber);
                    billingMap.put(INVOICE_NO, invoiceNo);
                    billingMap.put(INVOICE_DATE, invoiceDate);

                    accountInvoices.add(billingMap);

                    // Accumulate the charges
                    totalCharges += billingDto.getCharges();
                }
            }

            // Create a summary map for the account number with total charges and invoice date
            Map<String, Object> summaryMap = new LinkedHashMap<>();
            summaryMap.put(ACCOUNT_NUMBER, accountNumber);
            summaryMap.put(INVOICE_DATE, invoiceDate);
            summaryMap.put(INVOICE_NO, invoiceNo);
            summaryMap.put(TOTAL_CHARGES, totalCharges);
            summaryMap.put(ADDRESS, account.getAddress());
            summaryMap.put(TAX_NO, account.getTaxDocumentNo());
            summaryMap.put(TAX_INVOICE_NO, account.getCustomerName());
            summaryMap.put(INVOICES, accountInvoices);

            // Add the summary map to the result list
            result.add(summaryMap);

            // Increment the invoice number for the next account
            Long nextInvoiceNo = Long.parseLong(invoiceNo);
            nextInvoiceNo++;
            if (invoiceNo.matches("^0\\d+$")) {
                invoiceNo = String.format("%0" + invoiceNo.length() + "d", nextInvoiceNo);
            } else {
                invoiceNo = String.valueOf(nextInvoiceNo);
            }
        }

        return result;
    }

    public BillingDto toDto(Billing billing) {
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
                .taxAmount(billing.getTaxAmount())
                .isEmailSend(billing.getIsEmailSend())
                .build();
    }

    public Billing toEntity(BillingDto billingDto) {
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
                .taxAmount(billingDto.getTaxAmount())
                .isEmailSend(billingDto.getIsEmailSend())
                .build();
    }
}
