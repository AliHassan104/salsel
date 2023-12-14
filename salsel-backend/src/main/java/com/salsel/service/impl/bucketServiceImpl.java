package com.salsel.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.salsel.service.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class bucketServiceImpl implements BucketService {
    @Autowired
    private AmazonS3 s3Client;
    @Value("${application.bucket.name}")
    String bucketName;
    private static final Logger logger = LoggerFactory.getLogger(bucketServiceImpl.class);

    @Override
    public String save(byte[] pdf, String fileName) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdf);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(pdf.length);

            s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));

            // Generate pre-signed URL for the saved object
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, fileName)
                            .withMethod(HttpMethod.GET);

            URL preSignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            return preSignedUrl.toString();
        } catch (Exception e) {
            logger.error("File not uploaded to S3 bucket", fileName);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String fileName) {
        try {
            S3Object s3Object = s3Client.getObject(bucketName, fileName);

            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                return IOUtils.toByteArray(inputStream);
            }
            // Ensure the input stream is closed
        } catch (IOException e) {
            logger.error("cannot download file from s3 bucket");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
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

//    @Override
//    public List<String> getAllFiles() {
//        List<String> fileNames = new ArrayList<>();
//
//        ObjectListing objectListing = s3Client.listObjects(bucketName);
//
//        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//            fileNames.add(objectSummary.getKey());
//        }
//        return fileNames;
//    }

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

