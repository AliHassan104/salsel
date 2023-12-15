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
    public ResponseEntity<String> generateBarcode(@PathVariable Long id) {
        String data = "900000001";
        String url = codeGenerationService.generateBarcode(data, id);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/generate-vertical-barcode/awb/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> generateVerticalBarcode(@PathVariable Long id) {
        String data = "900000001";
        String url = codeGenerationService.generateBarcodeVertical(data, id);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/generate-qr-code/awb/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> generateQRCode(@PathVariable Long id) {
        String data = "https://example.com";
        String url = codeGenerationService.generateQRCode(data, id);
        return ResponseEntity.ok(url);

    }
}
