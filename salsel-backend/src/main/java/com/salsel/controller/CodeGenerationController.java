package com.salsel.controller;

import com.salsel.service.CodeGenerationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class CodeGenerationController {
    private final CodeGenerationService codeGenerationService;

    public CodeGenerationController(CodeGenerationService codeGenerationService) {
        this.codeGenerationService = codeGenerationService;
    }

    @GetMapping("/generate-barcode/awb/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> generateBarcode(HttpServletResponse response, @PathVariable Long id) {
        try {
            String data = "900000001";
            boolean success = codeGenerationService.generateBarcode(data, id, response.getOutputStream());

            if (success) {
                return ResponseEntity.ok("Barcode generated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate barcode");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while generating barcode");
        }
    }

    @GetMapping("/generate-qr-code/awb/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> generateQRCode(HttpServletResponse response, @PathVariable Long id) {
        try {
            String data = "https://example.com";
            boolean success = codeGenerationService.generateQRCode(data, id, response.getOutputStream());

            if (success) {
                return ResponseEntity.ok("QR code generated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate QR code");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while generating QR code");
        }
    }
}
