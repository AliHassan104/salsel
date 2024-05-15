package com.salsel.controller;

import com.salsel.service.BucketService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BucketController {
    private final BucketService bucketService;

    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> upload(@RequestParam(name = "file") MultipartFile multipartFile) {
        try {
            byte[] fileBytes = multipartFile.getBytes();
            String fileName = multipartFile.getOriginalFilename();

            // Save the file to the bucket and get the URL
//            String fileUrl = bucketService.save(fileBytes, fileName);

            // Add any additional logic or response as needed
//            return ResponseEntity.ok().body("File uploaded successfully. URL: " + fileUrl);
            return ResponseEntity.ok().body("File uploaded successfully. URL: ");

        } catch (IOException e) {
            // Handle exception (e.g., log, return error response)
            e.printStackTrace();  // Log the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading the file: " + e.getMessage());
        }
    }

//    @GetMapping("/file")
//    public ResponseEntity<List<String>> getAllFiles() {
//        List<String> fileNames = bucketService.getAllFiles();
//        return ResponseEntity.ok(fileNames);
//    }

    @GetMapping("/files")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> getAllFilesWithUrls() {
        Map<String, String> fileDetailsMap = bucketService.getAllFilesWithUrls();
        return ResponseEntity.ok(fileDetailsMap);
    }

    @GetMapping("/file/{folderType}/{folderName}/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String folderName, @PathVariable String fileName,
                                               @PathVariable String folderType) {
        try {
            byte[] fileContent = bucketService.downloadFile(folderName, fileName, folderType);

            // Return the file content as a ResponseEntity with appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileContent);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error downloading file: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/file/{folderType}/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName,
                                               @PathVariable String folderType) {
        try {
            byte[] fileContent = bucketService.downloadFile(fileName, folderType);

            // Return the file content as a ResponseEntity with appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileContent);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error downloading file: " + e.getMessage()).getBytes());
        }
    }

    @DeleteMapping("/{fileName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteFile(@PathVariable String fileName) {
        bucketService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/folder/{folderName}")
    public ResponseEntity<String> deleteFolder(@PathVariable String folderName) {
        try {
            bucketService.deleteFolder(folderName);
            return ResponseEntity.ok("Folder deleted successfully: " + folderName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting folder: " + e.getMessage());
        }
    }
}
