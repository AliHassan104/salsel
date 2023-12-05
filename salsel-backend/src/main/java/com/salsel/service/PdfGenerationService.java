package com.salsel.service;

import org.springframework.ui.Model;
import org.thymeleaf.context.Context;

public interface PdfGenerationService {
    byte[] generatePdf(String templateName, Model model);
}
