package com.salsel.scheduler;

import com.salsel.dto.BillingDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.*;
import com.salsel.repository.*;
import com.salsel.service.BucketService;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.PdfGenerationService;
import com.salsel.service.StatementService;
import com.salsel.service.impl.BillingServiceImpl;
import com.salsel.utils.EmailUtils;
import com.salsel.utils.HelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.salsel.constants.BillingConstants.ACCOUNT_NUMBER;

@Component
@Slf4j
public class EmailScheduler {
    private final EmailUtils emailUtils;
    private final TicketRepository ticketRepository;
    private final BillingRepository billingRepository;
    private final AccountRepository accountRepository;
    private final BillingServiceImpl billingService;
    private final StatementService statementService;
    private final BillingAttachmentRepository billingAttachmentRepository;
    private final HelperUtils helperUtils;
    private final StatementRepository statementRepository;
    private final BucketService bucketService;
    private final PdfGenerationService pdfGenerationService;
    private final ExcelGenerationService excelGenerationService;

    public EmailScheduler(EmailUtils emailUtils, TicketRepository ticketRepository, BillingRepository billingRepository, AccountRepository accountRepository, BillingServiceImpl billingService, StatementService statementService, BillingAttachmentRepository billingAttachmentRepository, HelperUtils helperUtils, StatementRepository statementRepository, BucketService bucketService, PdfGenerationService pdfGenerationService, ExcelGenerationService excelGenerationService) {
        this.emailUtils = emailUtils;
        this.ticketRepository = ticketRepository;
        this.billingRepository = billingRepository;
        this.accountRepository = accountRepository;
        this.billingService = billingService;
        this.statementService = statementService;
        this.billingAttachmentRepository = billingAttachmentRepository;
        this.helperUtils = helperUtils;
        this.statementRepository = statementRepository;
        this.bucketService = bucketService;
        this.pdfGenerationService = pdfGenerationService;
        this.excelGenerationService = excelGenerationService;
    }

    @Scheduled(cron = "0 5 0 * * ?") // Execute every day at 12:05 AM
    public void sendDailyEmailReport() {
        // Get the current date and time
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Check if it's after 5 minutes past midnight
        if (currentTime.isAfter(LocalTime.of(0, 5))) {
            // Increment the current date to the next day
            currentDate = currentDate.plusDays(1);
        }

        // Construct the  recipient email addresses
        String toAddress = "a.choudhary@salassilexpress.com";
        String[] ccAddresses = {"ashraf@salassilexpress.com", "samer@salassilexpress.com", "muhammadtabish05@gmail.com"};

        // Send the email
        emailUtils.sendEmail(toAddress, ccAddresses);

        // Log or handle success/failure
        System.out.println("Daily email report sent for " + currentDate);
    }

    @Scheduled(cron = "0 6 0 * * ?") // Execute every day at 12:06 AM
    public void sendEmailForPendingTickets() {
        List<Ticket> pendingTickets = ticketRepository.getAllTicketsWhereStatusIsNotClosed();
        if (!pendingTickets.isEmpty()) {
            String toAddress = "a.choudhary@salassilexpress.com";
            String[] ccAddresses = {"ashraf@salassilexpress.com", "samer@salassilexpress.com", "muhammadtabish05@gmail.com"};
            try {
                emailUtils.sendAlertEmailForPendingTickets(pendingTickets, toAddress, ccAddresses);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Transactional
    @Scheduled(cron = "0 */5 * * * ?") // Execute every 5 minutes
    public void sendEmailForBillingReport() {
        List<Billing> billings = billingRepository.findAllInDesOrderByIdAndStatus(true);
        if (billings != null) {
            List<BillingDto> billingDtoList = billings.stream()
                    .filter(billing -> billing.getIsEmailSend() == null || !billing.getIsEmailSend())
                    .map(billingService::toDto)
                    .collect(Collectors.toList());

            List<Map<String, Object>> billingMapList = billingService.getBillingInvoiceDataByExcelUploaded(billingDtoList);
            if (billingMapList != null) {
                for (Map<String, Object> invoice : billingMapList) {
                    Long accountNumber = (Long) invoice.get(ACCOUNT_NUMBER);
                    Account account = accountRepository.findByAccountNumber(accountNumber);
                    if (account != null) {
                        String toAddress = account.getEmail();
                        if (toAddress != null) {
                            BillingAttachment billingAttachment = billingAttachmentRepository.findFirstByAccountNumberOrderByCreatedAtDesc(accountNumber)
                                    .orElseThrow(() -> new RecordNotFoundException(String.format("Billing Attachments not found for accountNumber ==> %d", accountNumber)));

                            String pdfUrl = billingAttachment.getPdfUrl();
                            String excelUrl = billingAttachment.getExcelUrl();
                            if (pdfUrl != null && excelUrl != null) {
                                byte[] pdfContent = helperUtils.downloadAttachment(pdfUrl);
                                byte[] excelContent = helperUtils.downloadAttachment(excelUrl);
                                emailUtils.sendBillingEmailWithAttachments(toAddress, pdfContent, excelContent);
                                helperUtils.updateBillingStatus(accountNumber);
                                log.info("Email sent with both excel and pdf to address: " + toAddress);
                                helperUtils.sleepForDelay();
                            } else {
                                throw new RecordNotFoundException("PDF or Excel URL is null for account: " + accountNumber);
                            }
                        } else {
                            throw new RecordNotFoundException("No email found in Account: " + accountNumber);
                        }
                    }
                }
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 2/5 * * * ?") // Run 2 minutes after every 5-minute mark
    public void sendEmailForCustomerStatement() {
        List<Statement> statements = statementRepository.findAllByIsEmailSendFalse();
        if (statements != null) {

            List<Map<String, Object>> statementMapList = statementService.addStatements();
            if (statementMapList != null) {
                for (Map<String, Object> statement : statementMapList) {
                    Long accountNumber = (Long) statement.get(ACCOUNT_NUMBER);
                    Account account = accountRepository.findByAccountNumber(accountNumber);
                    if (account != null) {
                        String toAddress = account.getEmail();
                        if (toAddress != null) {
                            Statement statement1 = statementRepository.findFirstByAccountNumberOrderByCreatedAtDesc(accountNumber)
                                    .orElseThrow(() -> new RecordNotFoundException(String.format("Statement not found for accountNumber ==> %d", accountNumber)));

                            String excelUrl = statement1.getUrl();
                            if (excelUrl != null) {
                                byte[] excelContent = helperUtils.downloadAttachment(excelUrl);
                                emailUtils.sendStatementEmail(toAddress, excelContent);
                                helperUtils.updateStatementStatus(accountNumber);
                                log.info("Email sent with Statement excel to address: " + toAddress);
                                helperUtils.sleepForDelay();
                            } else {
                                throw new RecordNotFoundException("Excel URL is null for account: " + accountNumber);
                            }
                        } else {
                            throw new RecordNotFoundException("No email found in Account: " + accountNumber);
                        }
                    }
                }
            }
        }
    }

}
