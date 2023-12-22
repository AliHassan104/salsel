package com.salsel.service.impl;

import com.salsel.exception.RecordNotFoundException;
import com.salsel.service.BucketService;
import com.salsel.service.UploadPdfService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadPdfServiceImpl implements UploadPdfService {
    private final BucketService bucketService;
    private static final long MAX_FILE_SIZE_BYTES = 50 * 1024 * 1024;
    private static final Logger logger = LoggerFactory.getLogger(UploadPdfServiceImpl.class);

    public UploadPdfServiceImpl(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @Override
    public Map<String, String> uploadPdf(MultipartFile pdf) {
        Map<String, String> responseMap = new HashMap<>();

        // Check if the file is empty
        if (pdf.isEmpty()) {
            responseMap.put("response", "failed");
            responseMap.put("message", "File not found.");
            return responseMap;
        }

        // Check if the file size exceeds the allowed limit
        if (pdf.getSize() > MAX_FILE_SIZE_BYTES) {
            responseMap.put("response", "failed");
            responseMap.put("message", "File size exceeds the allowed limit.");
            return responseMap;
        }

        // Check if the file is a PDF
        if (!MediaType.APPLICATION_PDF.isCompatibleWith(MediaType.parseMediaType(pdf.getContentType()))) {
            responseMap.put("response", "failed");
            responseMap.put("message", "Invalid file format. Only PDF files are allowed.");
            return responseMap;
        }

        try {
            String folderName = "Agreements";
            String originalFileName = pdf.getOriginalFilename();

            // Extract file extension using FilenameUtils
            String fileExtension = "." + FilenameUtils.getExtension(originalFileName);

            // Generate timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));

            // Append timestamp to the original filename
            String newFileName = FilenameUtils.getBaseName(originalFileName) + "_" + timestamp + fileExtension;

            // Save to S3 bucket
            bucketService.save(pdf.getBytes(), folderName, newFileName);
            logger.info(newFileName + " is uploaded to S3.");

            // Populate the response map for success
            responseMap.put("response", "success");
            responseMap.put("message", "File uploaded successfully.");
            return responseMap;

        } catch (IOException e) {
            // Populate the response map for failure
            responseMap.put("response", "failed");
            responseMap.put("message", "Failed to upload: " + e.getMessage());
            return responseMap;
        }
    }
}
