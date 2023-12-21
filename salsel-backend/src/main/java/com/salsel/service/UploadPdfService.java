package com.salsel.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadPdfService {
    String uploadPdf(MultipartFile pdf);
}
