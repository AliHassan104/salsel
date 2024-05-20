package com.salsel.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.service.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class bucketServiceImpl implements BucketService {

    public static final String ACCOUNT = "Account";
    public static final String LOGO = "Logo";
    public static final String AWB = "Awb";
    public static final String TICKET = "Ticket";
    public static final String EMPLOYEE = "Employee";
    public static final String INVOICE = "Invoice";
    @Autowired
    private AmazonS3 s3Client;
    @Value("${application.bucket.name}")
    String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);

    @Override
    public String save(byte[] pdf, String folderName, String fileName, String folderType) {
        try {
            // Validate input parameters
            if (pdf == null || folderName == null || fileName == null || folderType == null) {
                throw new IllegalArgumentException("Invalid input parameters");
            }

            // Check if the folder exists
            String folderKey;
            if (folderType.equalsIgnoreCase(ACCOUNT)) {
                folderKey = ACCOUNT + "/" + folderName;
            } else if (folderType.equalsIgnoreCase(AWB)) {
                folderKey = AWB + "/" + folderName;
            } else if (folderType.equalsIgnoreCase(TICKET)) {
                folderKey = TICKET + "/" + folderName;
            } else if (folderType.equalsIgnoreCase(EMPLOYEE)) {
                folderKey = EMPLOYEE + "/" + folderName;
            } else if (folderType.equalsIgnoreCase(INVOICE)) {
                folderKey = INVOICE + "/" + folderName;
            }
            else {
                throw new RecordNotFoundException("Invalid folder type: " + folderType);
            }

            if (!s3Client.doesObjectExist(bucketName, folderKey + "/")) {
                // Create an empty object to simulate the directory
                s3Client.putObject(bucketName, folderKey + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
            }

            // Use try-with-resources to automatically close the input stream
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdf)) {
                String key = folderKey + "/" + fileName;
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(pdf.length);

                s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));
                return key;
            }
        } catch (Exception e) {
            logger.error("File {} not uploaded to S3 bucket", fileName, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<String> saveMultipleFiles(Map<String, byte[]> files, String folderName, String folderType) {
        try {
            // Validate input parameters
            if (files == null || folderName == null || folderType == null) {
                throw new IllegalArgumentException("Invalid input parameters");
            }

            List<String> fileKeys = new ArrayList<>();

            // Check if the folder exists
            String folderKey;
            if (folderType.equalsIgnoreCase(ACCOUNT)) {
                folderKey = ACCOUNT + "/" + folderName;
            } else if (folderType.equalsIgnoreCase(AWB)) {
                folderKey = AWB + "/" + folderName;
            } else if (folderType.equalsIgnoreCase(TICKET)) {
                folderKey = TICKET + "/" + folderName;
            } else {
                throw new RecordNotFoundException("Invalid folder type: " + folderType);
            }

            if (!s3Client.doesObjectExist(bucketName, folderKey + "/")) {
                // Create an empty object to simulate the directory
                s3Client.putObject(bucketName, folderKey + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
            }

            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                String fileName = entry.getKey();
                byte[] pdf = entry.getValue();

                // Use try-with-resources to automatically close the input stream
                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdf)) {
                    String key = folderKey + "/" + fileName;
                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(pdf.length);

                    s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));
                    fileKeys.add(key);
                }
            }

            return fileKeys;
        } catch (Exception e) {
            logger.error("Files not uploaded to S3 bucket", e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public byte[] downloadFile(String folderName, String fileName, String folderType) {
        try {
            String key = folderType + "/" + folderName + "/" + fileName;

            // Check if the file exists in the S3 bucket
            if (!s3Client.doesObjectExist(bucketName, key)) {
                throw new FileNotFoundException("File not found: " + key);
            }

            S3Object s3Object = s3Client.getObject(bucketName, key);

            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                return IOUtils.toByteArray(inputStream);
            }
            // Ensure the input stream is closed
        } catch (IOException e) {
            logger.error("Error downloading file from S3 bucket: {}", fileName, e);
            throw new RuntimeException("Error downloading file from S3 bucket: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String fileName, String folderType) {
        try {
            String key = folderType + "/" + fileName;

            // Check if the file exists in the S3 bucket
            if (!s3Client.doesObjectExist(bucketName, key)) {
                throw new FileNotFoundException("File not found: " + key);
            }

            S3Object s3Object = s3Client.getObject(bucketName, key);

            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                return IOUtils.toByteArray(inputStream);
            }
            // Ensure the input stream is closed
        } catch (IOException e) {
            logger.error("Error downloading file from S3 bucket: {}", fileName, e);
            throw new RuntimeException("Error downloading file from S3 bucket: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
            logger.info("File deleted successfully: {}", fileName);
        } catch (Exception e) {
            logger.error("Error deleting file from S3 bucket: {}", fileName);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteFileAtPath(String folderKey, String fileName) {
        try {
            String objectKey = folderKey + "/" + fileName;
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));

            logger.info("File deleted successfully: {}", objectKey);
        } catch (Exception e) {
            logger.error("Error deleting file {} from path {} in S3 bucket: {}", fileName, folderKey, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void deleteFilesStartingWith(String folderKey, String prefix) {
        try {
            // List the objects in the specified folder
            ObjectListing objectListing = s3Client.listObjects(bucketName, folderKey);

            // Iterate through the objects and delete files with names starting with the specified prefix
            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                String objectKey = summary.getKey();

                // Check if the object is a file and its name starts with the specified prefix
                if (objectKey.startsWith(folderKey + "/" + prefix)) {
                    String file = folderKey + "/" + prefix;
                    s3Client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
                    logger.info("File deleted successfully: {}", objectKey);
                }
            }
        } catch (Exception e) {
            logger.error("Error deleting files from path {} in S3 bucket with prefix {}: {}", folderKey, prefix, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }



    @Override
    public void deleteFolder(String folderName) {
        ObjectListing objectListing = s3Client.listObjects(bucketName, folderName);

        // Iterate through each object in the folder and delete it
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            s3Client.deleteObject(bucketName, objectSummary.getKey());
        }

        // Delete the folder itself
        s3Client.deleteObject(bucketName, folderName);
    }

    @Override
    public Map<String, String> getAllFilesWithUrls() {
        Map<String, String> fileDetailsMap = new HashMap<>();

        ObjectListing objectListing = s3Client.listObjects(bucketName);

        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String fileName = objectSummary.getKey();

            // Generate pre-signed URL for the file
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, fileName)
                            .withMethod(HttpMethod.GET);

            URL preSignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            // Add the file name and pre-signed URL to the map
            fileDetailsMap.put(fileName, preSignedUrl.toString());
        }

        return fileDetailsMap;
    }
}

