package com.salsel.controller;

import com.salsel.service.UploadPdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadPdfController {
    private final UploadPdfService uploadPdfService;

    public UploadPdfController(UploadPdfService uploadPdfService) {
        this.uploadPdfService = uploadPdfService;
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<Map<String, String>> uploadPdf(@RequestParam("file") MultipartFile pdf) {
        Map<String, String> responseMap = uploadPdfService.uploadPdf(pdf);

        if ("success".equals(responseMap.get("response"))) {
            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.badRequest().body(responseMap);
        }
    }
}
