package com.salsel.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadPdfService {
    Map<String,String> uploadPdf(MultipartFile pdf);
}
