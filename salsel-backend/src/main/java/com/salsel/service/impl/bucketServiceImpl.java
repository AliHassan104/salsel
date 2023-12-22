package com.salsel.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.salsel.service.BucketService;
import com.salsel.utils.HelperUtils;
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
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class bucketServiceImpl implements BucketService {

    public static final String ACCOUNT = "Account";
    public static final String AWB = "Awb";
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

            // Create the folder if it doesn't exist
            createFolderIfNotExists(folderName, folderType);

            // Use try-with-resources to automatically close the input stream
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdf)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(pdf.length);

                String key = null;
                if (folderType.equalsIgnoreCase(ACCOUNT)) {
                    key = ACCOUNT + "/" + folderName + "/" + fileName;
                } else if (folderType.equalsIgnoreCase(AWB)) {
                    key = AWB + "/" + folderName + "/" + fileName;
                } else {
                    throw new IllegalArgumentException("Invalid folder type: " + folderType);
                }

                s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));

                // Generate pre-signed URL for the saved object
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, key)
                                .withMethod(HttpMethod.GET);

                URL preSignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

                return preSignedUrl.toString();
            }
        } catch (Exception e) {
            logger.error("File {} not uploaded to S3 bucket", fileName, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String folderName, String fileName) {
        try {
            String key = folderName + "/" + fileName;

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

    private void createFolderIfNotExists(String folderName, String folderType) {
        try {
            // Check if the folder already exists

            if(folderType.equalsIgnoreCase(AWB)){
                if (!s3Client.doesObjectExist(bucketName, AWB + "/" + folderName + "/")) {
                    // If not, create the folder
                    s3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
                    logger.info("Folder '{}' created in S3 bucket", folderName);
                }
            } else if (folderType.equalsIgnoreCase(ACCOUNT)) {
                if (!s3Client.doesObjectExist(bucketName, ACCOUNT + "/" + folderName + "/")) {
                    // If not, create the folder
                    s3Client.putObject(bucketName, folderName + "/", new ByteArrayInputStream(new byte[0]), new ObjectMetadata());
                    logger.info("Folder '{}' created in S3 bucket", folderName);
                }
            }

        } catch (Exception e) {
            logger.error("Error creating folder '{}' in S3 bucket", folderName, e);
            throw new RuntimeException(e.getMessage());
        }
    }
}

