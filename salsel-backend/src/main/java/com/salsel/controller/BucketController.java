package com.salsel.controller;

import com.salsel.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            String fileUrl = bucketService.save(fileBytes, fileName);

            // Add any additional logic or response as needed
            return ResponseEntity.ok().body("File uploaded successfully. URL: " + fileUrl);

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

    @GetMapping("file/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileContent = bucketService.downloadFile(fileName);

//            // Create HttpHeaders with appropriate content type and disposition
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData("attachment", fileName);

            // Return the file content as a ResponseEntity with appropriate headers
            return new ResponseEntity<>(fileContent, HttpStatus.OK);

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
}
