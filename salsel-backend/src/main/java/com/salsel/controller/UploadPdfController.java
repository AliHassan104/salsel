package com.salsel.controller;

import com.salsel.service.UploadPdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UploadPdfController {
    private final UploadPdfService uploadPdfService;

    public UploadPdfController(UploadPdfService uploadPdfService) {
        this.uploadPdfService = uploadPdfService;
    }

    @PostMapping("/upload-pdf")
    @PreAuthorize("hasAuthority('CREATE_ACCOUNT') and hasAuthority('READ_ACCOUNT')")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        String imageUrl = uploadPdfService.uploadPdf(file);
        return ResponseEntity.ok(imageUrl);
    }
}
