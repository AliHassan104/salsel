package com.salsel.service;

import org.springframework.ui.Model;

public interface PdfGenerationService {
    byte[] generatePdf(String templateName, Model model, Long awbId);
    byte[] generateEmployeePdf(Long empId);
    byte[] generateBillingPdf();

    byte[] generateSalassilStatementPdf();

}
