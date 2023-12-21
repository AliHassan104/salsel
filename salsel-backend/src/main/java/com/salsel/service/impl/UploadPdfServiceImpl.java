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

@Service
public class UploadPdfServiceImpl implements UploadPdfService {
    private final BucketService bucketService;
    private static final long MAX_FILE_SIZE_BYTES = 50 * 1024 * 1024;
    private static final Logger logger = LoggerFactory.getLogger(UploadPdfServiceImpl.class);

    public UploadPdfServiceImpl(BucketService bucketService) {
        this.bucketService = bucketService;
    }
    @Override
    public String uploadPdf(MultipartFile pdf) {
        // Check if the file is empty
        if (pdf.isEmpty()) {
            throw new RecordNotFoundException("File not found.");
        }

        // Check if the file size exceeds the allowed limit
        if (pdf.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new RecordNotFoundException("File size exceeds the allowed limit.");
        }

        // Check if the file is a PDF
        if (!MediaType.APPLICATION_PDF.isCompatibleWith(MediaType.parseMediaType(pdf.getContentType()))) {
            throw new RecordNotFoundException("Invalid file format. Only PDF files are allowed.");
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
            String url = bucketService.save(pdf.getBytes(), folderName, newFileName);
            logger.info(newFileName + " is uploaded to S3.");
            return url;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload: " + e.getMessage());
        }
    }
}
