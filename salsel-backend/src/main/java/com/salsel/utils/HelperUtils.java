package com.salsel.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.salsel.model.Otp;
import com.salsel.service.BucketService;
import com.salsel.service.impl.bucketServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class HelperUtils {
    @Autowired
    private AmazonS3 s3Client;
    @Autowired
    private BucketService bucketService;
    @Value("${application.bucket.name}")
    String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);

    public boolean isValidResetCode(Otp otp, String resetCode) {
        if (otp.getResetCode() == null || !otp.getResetCode().equals(resetCode)) {
            return false;
        }
        LocalDateTime resetCodeTimestamp = otp.getResetCodeTimestamp();
        LocalDateTime currentTimestamp = LocalDateTime.now();
        Duration duration = Duration.between(resetCodeTimestamp, currentTimestamp);
        long expirationTimeInMinutes = 02;
        return duration.toMinutes() <= expirationTimeInMinutes;
    }

    public String generateResetCode() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 6);
    }

    public void createFolderIfNotExists(String folderName) {
        try {
            // Check if the folder already exists
            if (!s3Client.doesObjectExist(bucketName, folderName + "/")) {
                // If not, create the folder
                s3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
                logger.info("Folder '{}' created in S3 bucket", folderName);
            }
        } catch (Exception e) {
            logger.error("Error creating folder '{}' in S3 bucket", folderName, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public String savePdfToS3(MultipartFile pdf, String folderName) {
        try {
            String originalFileName = pdf.getOriginalFilename();

            // Extract file extension using FilenameUtils
            String fileExtension = "." + FilenameUtils.getExtension(originalFileName);

            // Generate timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));

            // Append timestamp to the original filename
            String newFileName = FilenameUtils.getBaseName(originalFileName) + "_" + timestamp + fileExtension;

            // Save to S3 bucket
            createFolderIfNotExists(folderName); // Create folder
            return bucketService.save(pdf.getBytes(), folderName, newFileName); // Save PDF and return the URL

        } catch (IOException e) {
            logger.error("Failed to save PDF to S3", e);
            throw new RuntimeException("Failed to save PDF to S3: " + e.getMessage());
        }
    }


}
