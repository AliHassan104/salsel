package com.salsel.service.impl;

import com.salsel.dto.StatementDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Billing;
import com.salsel.model.Statement;
import com.salsel.repository.BillingRepository;
import com.salsel.repository.StatementRepository;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.StatementService;
import com.salsel.utils.HelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.salsel.constants.BillingConstants.*;

@Service
@Slf4j
public class StatementServiceImpl implements StatementService {
    private final StatementRepository statementRepository;
    private final BillingRepository billingRepository;
    private final ExcelGenerationService excelGenerationService;
    private final HelperUtils helperUtils;

    public StatementServiceImpl(StatementRepository statementRepository, BillingRepository billingRepository, ExcelGenerationService excelGenerationService, HelperUtils helperUtils) {
        this.statementRepository = statementRepository;
        this.billingRepository = billingRepository;
        this.excelGenerationService = excelGenerationService;
        this.helperUtils = helperUtils;
    }
//
//    @Transactional
//    @Override
//    public List<Map<String, Object>> addStatements() {
//        List<Billing> billings = billingRepository.findAllByStatementIdIsNull();
//        List<Map<String, Object>> accountStatements = new ArrayList<>();
//        Set<Long> processedAccounts = new HashSet<>();
//
//        for (Billing billing : billings) {
//            Long accountNumber = billing.getCustomerAccountNumber();
//            if (processedAccounts.contains(accountNumber)) {
//                continue;
//            }
//
//            List<Billing> billingsByAccount = billings.stream()
//                    .filter(b -> b.getCustomerAccountNumber().equals(accountNumber))
//                    .collect(Collectors.toList());
//
//            List<Map<String, Object>> statementEntries = new ArrayList<>();
//            int count = 0;
//
//            for (Billing accountBilling : billingsByAccount) {
//                Map<String, Object> statementMap = new LinkedHashMap<>();
//                statementMap.put(SNo, ++count);
//                statementMap.put(INVOICE_NO, accountBilling.getInvoiceNo());
//                statementMap.put(INVOICE_DATE, accountBilling.getInvoiceDate());
//                statementMap.put(INVOICE_TYPE, accountBilling.getInvoiceType());
//                statementMap.put(CUSTOMER_REF, accountBilling.getCustomerRef());
//
//                Double debit = accountBilling.getCharges();
//                Double credit = 0.0;
//                Double net = debit - credit;
//
//                statementMap.put(DEBIT, debit);
//                statementMap.put(CREDIT, credit);
//                statementMap.put(NET, net);
//
//                statementEntries.add(statementMap);
//            }
//
//            for (Map<String, Object> entry : statementEntries) {
//                Statement createdStatement = new Statement();
//                createdStatement.setAccountNumber(accountNumber);
//                createdStatement.setCustomerName(billing.getTaxInvoiceTo());
//                createdStatement.setInvoiceNo((String) entry.get(INVOICE_NO));
//                createdStatement.setInvoiceDate((LocalDate) entry.get(INVOICE_DATE));
//                createdStatement.setInvoiceType((String) entry.get(INVOICE_TYPE));
//                createdStatement.setCustomerRef((String) entry.get(CUSTOMER_REF));
//                createdStatement.setDebit((Double) entry.get(DEBIT));
//                createdStatement.setCredit((Double) entry.get(CREDIT));
//                createdStatement.setNet((Double) entry.get(NET));
//                createdStatement.setCreatedAt(LocalDate.now());
//                createdStatement.setStatus(true);
//                createdStatement.setIsEmailSend(false);
//                Statement savedStatement = statementRepository.save(createdStatement);
//
//                List<Billing> existingBillings = billingRepository.findByInvoiceNo(createdStatement.getInvoiceNo())
//                        .orElseThrow(() -> new RecordNotFoundException("Billing not found for invoice no: " + createdStatement.getInvoiceNo()));
//
//                for(Billing bill : existingBillings){
//                    bill.setStatementId(savedStatement.getId());
//                    billingRepository.save(bill);
//                }
//            }
//
//            Map<String, Object> accountMap = new LinkedHashMap<>();
//            accountMap.put(STATEMENT_OF_ACCOUNT_AS_OF, LocalDate.now().toString());
//            accountMap.put(ACCOUNT_NUMBER, accountNumber);
//            accountMap.put(CUSTOMER_NAME, billing.getTaxInvoiceTo());
//            accountMap.put(STATEMENT, statementEntries);
//
//            accountStatements.add(accountMap);
//            processedAccounts.add(accountNumber);
//        }
//
//        Map<Long, List<ByteArrayOutputStream>> excelFiles = excelGenerationService.generateBillingStatements(accountStatements);
//
//        if (excelFiles != null && !excelFiles.isEmpty()) {
//            for (Map.Entry<Long, List<ByteArrayOutputStream>> excelEntry : excelFiles.entrySet()) {
//                Long accountNumber = excelEntry.getKey();
//                List<ByteArrayOutputStream> excels = excelEntry.getValue();
//
//                List<byte[]> excelBytesList = new ArrayList<>();
//                for (ByteArrayOutputStream excelStream : excels) {
//                    excelBytesList.add(excelStream.toByteArray());
//                }
//
//                String folderName = "Statement_" + accountNumber;
//                List<String> savedExcelsUrls = helperUtils.saveStatementExcelListToS3(excelBytesList, folderName, STATEMENT);
//                List<Statement> statements = statementRepository.findAllByAccountNumberAndUrlNull(accountNumber);
//                for(Statement s : statements){
//                    s.setUrl(savedExcelsUrls.get(0));
//                    statementRepository.save(s);
//                }
//
//                log.info("Invoices are uploaded to S3 in folder '{}'.", folderName);
//            }
//        }
//        return accountStatements;
//    }
//

    @Transactional
    @Override
    public List<Map<String, Object>> addStatements() {
        List<Billing> billings = billingRepository.findAllByStatementIdIsNull();
        List<Map<String, Object>> accountStatements = generateAccountStatements(billings);

        Map<Long, List<ByteArrayOutputStream>> excelFiles = excelGenerationService.generateBillingStatements(accountStatements);
        if (excelFiles != null && !excelFiles.isEmpty()) {
            saveExcelFilesToS3(excelFiles);
        }

        return accountStatements;
    }

    private List<Map<String, Object>> generateAccountStatements(List<Billing> billings) {
        List<Map<String, Object>> accountStatements = new ArrayList<>();
        Set<Long> processedAccounts = new HashSet<>();

        for (Billing billing : billings) {
            Long accountNumber = billing.getCustomerAccountNumber();
            if (processedAccounts.contains(accountNumber)) {
                continue;
            }

            List<Billing> billingsByAccount = billings.stream()
                    .filter(b -> b.getCustomerAccountNumber().equals(accountNumber))
                    .collect(Collectors.toList());

            List<Map<String, Object>> statementEntries = new ArrayList<>();
            int count = 0;

            for (Billing accountBilling : billingsByAccount) {
                Map<String, Object> statementMap = new LinkedHashMap<>();
                statementMap.put(SNo, ++count);
                statementMap.put(INVOICE_NO, accountBilling.getInvoiceNo());
                statementMap.put(INVOICE_DATE, accountBilling.getInvoiceDate());
                statementMap.put(INVOICE_TYPE, accountBilling.getInvoiceType());
                statementMap.put(CUSTOMER_REF, accountBilling.getCustomerRef());

                Double debit = accountBilling.getCharges();
                Double credit = 0.0;
                Double net = debit - credit;

                statementMap.put(DEBIT, debit);
                statementMap.put(CREDIT, credit);
                statementMap.put(NET, net);

                statementEntries.add(statementMap);
            }

            for (Map<String, Object> entry : statementEntries) {
                Statement createdStatement = new Statement();
                createdStatement.setAccountNumber(accountNumber);
                createdStatement.setCustomerName(billing.getTaxInvoiceTo());
                createdStatement.setInvoiceNo((String) entry.get(INVOICE_NO));
                createdStatement.setInvoiceDate((LocalDate) entry.get(INVOICE_DATE));
                createdStatement.setInvoiceType((String) entry.get(INVOICE_TYPE));
                createdStatement.setCustomerRef((String) entry.get(CUSTOMER_REF));
                createdStatement.setDebit((Double) entry.get(DEBIT));
                createdStatement.setCredit((Double) entry.get(CREDIT));
                createdStatement.setNet((Double) entry.get(NET));
                createdStatement.setCreatedAt(LocalDate.now());
                createdStatement.setStatus(true);
                createdStatement.setIsEmailSend(false);
                Statement savedStatement = statementRepository.save(createdStatement);

                List<Billing> existingBillings = billingRepository.findByInvoiceNo(createdStatement.getInvoiceNo())
                        .orElseThrow(() -> new RecordNotFoundException("Billing not found for invoice no: " + createdStatement.getInvoiceNo()));

                for (Billing bill : existingBillings) {
                    bill.setStatementId(savedStatement.getId());
                    billingRepository.save(bill);
                }
            }

            Map<String, Object> accountMap = new LinkedHashMap<>();
            accountMap.put(STATEMENT_OF_ACCOUNT_AS_OF, LocalDate.now().toString());
            accountMap.put(ACCOUNT_NUMBER, accountNumber);
            accountMap.put(CUSTOMER_NAME, billing.getTaxInvoiceTo());
            accountMap.put(STATEMENT, statementEntries);

            accountStatements.add(accountMap);
            processedAccounts.add(accountNumber);
        }

        return accountStatements;
    }

    private void saveExcelFilesToS3(Map<Long, List<ByteArrayOutputStream>> excelFiles) {
        for (Map.Entry<Long, List<ByteArrayOutputStream>> excelEntry : excelFiles.entrySet()) {
            Long accountNumber = excelEntry.getKey();
            List<ByteArrayOutputStream> excels = excelEntry.getValue();

            List<byte[]> excelBytesList = new ArrayList<>();
            for (ByteArrayOutputStream excelStream : excels) {
                excelBytesList.add(excelStream.toByteArray());
            }

            String folderName = "Statement_" + accountNumber;
            List<String> savedExcelsUrls = helperUtils.saveStatementExcelListToS3(excelBytesList, folderName, STATEMENT);

            List<Statement> statements = statementRepository.findAllByAccountNumberAndUrlNull(accountNumber);
            for (Statement statement : statements) {
                if (!savedExcelsUrls.isEmpty()) {
                    statement.setUrl(savedExcelsUrls.get(0));
                    statementRepository.save(statement);
                }
            }

            log.info("Invoices are uploaded to S3 in folder '{}'.", folderName);
        }
    }


    @Override
    public StatementDto getStatementById() {
        return null;
    }

    public StatementDto toDto(Statement statement) {
        return StatementDto.builder()
                .id(statement.getId())
                .createdAt(statement.getCreatedAt())
                .customerName(statement.getCustomerName())
                .accountNumber(statement.getAccountNumber())
                .invoiceNo(statement.getInvoiceNo())
                .invoiceType(statement.getInvoiceType())
                .invoiceDate(statement.getInvoiceDate())
                .customerRef(statement.getCustomerRef())
                .debit(statement.getDebit())
                .credit(statement.getCredit())
                .net(statement.getNet())
                .url(statement.getUrl())
                .isEmailSend(statement.getIsEmailSend())
                .status(statement.getStatus())
                .build();
    }

    public Statement toEntity(StatementDto statementDto) {
        return Statement.builder()
                .id(statementDto.getId())
                .createdAt(statementDto.getCreatedAt())
                .customerName(statementDto.getCustomerName())
                .accountNumber(statementDto.getAccountNumber())
                .invoiceNo(statementDto.getInvoiceNo())
                .invoiceType(statementDto.getInvoiceType())
                .invoiceDate(statementDto.getInvoiceDate())
                .customerRef(statementDto.getCustomerRef())
                .debit(statementDto.getDebit())
                .credit(statementDto.getCredit())
                .net(statementDto.getNet())
                .url(statementDto.getUrl())
                .isEmailSend(statementDto.getIsEmailSend())
                .status(statementDto.getStatus())
                .build();
    }
}
