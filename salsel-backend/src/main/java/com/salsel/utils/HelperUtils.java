package com.salsel.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.salsel.dto.CustomUserDetail;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.*;
import com.salsel.repository.BillingRepository;
import com.salsel.repository.StatementRepository;
import com.salsel.repository.UserRepository;
import com.salsel.service.BucketService;
import com.salsel.service.impl.bucketServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.salsel.service.impl.bucketServiceImpl.EMPLOYEE;

@Component
public class HelperUtils {
    private final AmazonS3 s3Client;
    private final BucketService bucketService;
    private final BillingRepository billingRepository;
    private final StatementRepository statementRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${application.bucket.name}")
    String bucketName;

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);

    public HelperUtils(AmazonS3 s3Client, BucketService bucketService, BillingRepository billingRepository, StatementRepository statementRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.s3Client = s3Client;
        this.bucketService = bucketService;
        this.billingRepository = billingRepository;
        this.statementRepository = statementRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    public boolean isValidResetCode(Otp otp, String resetCode) {
        if (otp.getResetCode() == null || !otp.getResetCode().equals(resetCode)) {
            return false;
        }
        LocalDateTime resetCodeTimestamp = otp.getResetCodeTimestamp();
        LocalDateTime currentTimestamp = LocalDateTime.now();
        Duration duration = Duration.between(resetCodeTimestamp, currentTimestamp);
        long expirationTimeInMinutes = 5;
        return duration.toMinutes() <= expirationTimeInMinutes;
    }

    public String generateResetCode() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 6);
    }

    public String generateResetPassword() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8);
    }

    public String saveAccountPdfToS3(MultipartFile pdf, String folderName) {
        try {
            String filename = "Agreement";

            // Extract file extension using FilenameUtils
            String fileExtension = "." + FilenameUtils.getExtension(pdf.getOriginalFilename());

            // Generate timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));

            // Append timestamp to the original filename
            String newFileName = FilenameUtils.getBaseName(filename) + "_" + timestamp + fileExtension;

            // Save to S3 bucket
            return bucketService.save(pdf.getBytes(), folderName, newFileName, "Account"); // Save PDF and return the URL

        } catch (IOException e) {
            logger.error("Failed to save PDF to S3", e);
            throw new RuntimeException("Failed to save PDF to S3: " + e.getMessage());
        }
    }

    public String saveTicketPdfToS3(MultipartFile pdf, String folderName) {
        try {
            String filename = pdf.getOriginalFilename();

            // Extract file extension using FilenameUtils
            String fileExtension = "." + FilenameUtils.getExtension(pdf.getOriginalFilename());

            // Generate timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));

            // Append timestamp to the original filename
            String newFileName = FilenameUtils.getBaseName(filename) + "_" + timestamp + fileExtension;

            // Save to S3 bucket
            return bucketService.save(pdf.getBytes(), folderName, newFileName, "Ticket"); // Save PDF and return the URL

        } catch (IOException e) {
            logger.error("Failed to save PDF to S3", e);
            throw new RuntimeException("Failed to save PDF to S3: " + e.getMessage());
        }
    }

    public String savePdfToS3(MultipartFile pdf, String folderName, String fileName) {
        try {
            String fileExtension = "." + FilenameUtils.getExtension(pdf.getOriginalFilename());
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
            String newFileName = FilenameUtils.getBaseName(fileName) + "_" + timestamp + fileExtension;
            return bucketService.save(pdf.getBytes(), folderName, newFileName, "Employee"); // Save PDF and return the URL
        } catch (IOException e) {
            logger.error("Failed to save File to S3", e);
            throw new RuntimeException("Failed to save File to S3: " + e.getMessage());
        }
    }

    public void saveFileToBucketAndSetUrl(MultipartFile file, Employee employee, String fileType) {
        if (file != null && !file.isEmpty()) {
            String folderName = "Employee_" + employee.getId();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
            String fileName = fileType + "_" + timestamp + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            String url = null;
            try {
                url = bucketService.save(file.getBytes(), folderName, fileName, EMPLOYEE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Set URL based on fileType
            switch (fileType) {
                case "passport":
                    employee.setPassportFilePath(url);
                    break;
                case "id":
                    employee.setIdFilePath(url);
                    break;
                default:
                    break;
            }
        }
    }

//    public List<String> saveTicketPdfListToS3(List<MultipartFile> pdfFiles, String folderName) {
//        try {
//            List<String> savedPdfUrls = new ArrayList<>();
//
//            for (MultipartFile pdf : pdfFiles) {
//                String filename = pdf.getOriginalFilename();
//                String fileExtension = "." + FilenameUtils.getExtension(pdf.getOriginalFilename());
//                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
//                String newFileName = FilenameUtils.getBaseName(filename) + "_" + timestamp + fileExtension;
//
//                // Save to S3 bucket
//                String savedPdfUrl = bucketService.save(pdf.getBytes(), folderName, newFileName, "Ticket");
//                savedPdfUrls.add(savedPdfUrl);
//            }
//
//            return savedPdfUrls;
//        } catch (IOException e) {
//            logger.error("Failed to save PDFs to S3", e);
//            throw new RuntimeException("Failed to save PDFs to S3: " + e.getMessage());
//        }
//    }

    public List<String> savePdfListToS3(List<MultipartFile> pdfFiles, String folderName, String folderType) {
        try {
            List<String> savedPdfUrls = new ArrayList<>();

            for (MultipartFile pdf : pdfFiles) {
                String filename = pdf.getOriginalFilename();
                String fileExtension = "." + FilenameUtils.getExtension(pdf.getOriginalFilename());
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
                String newFileName = FilenameUtils.getBaseName(filename) + "_" + timestamp + fileExtension;

                // Save to S3 bucket
                String savedPdfUrl = bucketService.save(pdf.getBytes(), folderName, newFileName, folderType);
                savedPdfUrls.add(savedPdfUrl);
            }

            return savedPdfUrls;
        } catch (IOException e) {
            logger.error("Failed to save PDFs to S3", e);
            throw new RuntimeException("Failed to save PDFs to S3: " + e.getMessage());
        }
    }

    public List<String> saveInvoicePdfListToS3(List<byte[]> pdfFiles, String folderName, String folderType) {
        List<String> savedPdfUrls = new ArrayList<>();

        for (byte[] pdfContent : pdfFiles) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
            String newFileName = "invoice_Pdf_" + timestamp + ".pdf"; // You can adjust the naming convention as needed

            // Save to S3 bucket
            String savedPdfUrl = bucketService.save(pdfContent, folderName, newFileName, folderType);
            savedPdfUrls.add(savedPdfUrl);
        }

        return savedPdfUrls;
    }

    public List<String> saveInvoiceExcelListToS3(List<byte[]> excelFiles, String folderName, String folderType) {
        List<String> savedExcelUrls = new ArrayList<>();

        for (byte[] excelContent : excelFiles) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
            String newFileName = "invoice_Excel_" + timestamp + ".xlsx"; // You can adjust the naming convention as needed

            // Save to S3 bucket
            String savedExcelUrl = bucketService.save(excelContent, folderName, newFileName, folderType);
            savedExcelUrls.add(savedExcelUrl);
        }

        return savedExcelUrls;
    }

    public List<String> saveStatementExcelListToS3(List<byte[]> excelFiles, String folderName, String folderType) {
        List<String> savedExcelUrls = new ArrayList<>();

        for (byte[] excelContent : excelFiles) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
            String newFileName = "Statement_Excel_" + timestamp + ".xlsx"; // You can adjust the naming convention as needed

            // Save to S3 bucket
            String savedExcelUrl = bucketService.save(excelContent, folderName, newFileName, folderType);
            savedExcelUrls.add(savedExcelUrl);
        }

        return savedExcelUrls;
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetail) {
            String email = ((CustomUserDetail) principal).getEmail();
            return userRepository.findByEmailAndStatusIsTrue(email)
                    .orElseThrow(() -> new RecordNotFoundException("User not found"));
        } else {
            throw new RecordNotFoundException("User not Found");
        }
    }

    public byte[] downloadAttachment(String url) {
        String[] parts = url.split("/");
        String folderName = parts[1];
        String folderType = parts[0];
        String fileName = parts[2];
        return bucketService.downloadFile(folderName, fileName, folderType);
    }

    public void updateBillingStatus(Long accountNumber) {
        List<Billing> updatedBillings = billingRepository.findByCustomerAccountNumber(accountNumber);
        updatedBillings.forEach(billing -> {
            billing.setIsEmailSend(true);
            billingRepository.save(billing);
        });
    }

    public void updateStatementStatus(Long accountNumber) {
        List<Statement> updatedStatements = statementRepository.findAllByAccountNumber(accountNumber);
        updatedStatements.forEach(statement -> {
            statement.setIsEmailSend(true);
            statementRepository.save(statement);
        });
    }

    public void sleepForDelay() {
        // Add delay between sending emails
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public InputStream getInputStreamFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getInputStream();
    }

    public UserDetails authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            return userDetailsService.loadUserByUsername(email);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Incorrect Username or Password!", e);
        }
    }

    public void checkRole(UserDetails userDetails) {
        List<String> allowedRoles = Arrays.asList("ROLE_ADMIN", "ROLE_COURIER");

        boolean hasRequiredRole = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(allowedRoles::contains);

        if (!hasRequiredRole) {
            throw new BadCredentialsException("User does not have the required role. Access denied.");
        }
    }


}
