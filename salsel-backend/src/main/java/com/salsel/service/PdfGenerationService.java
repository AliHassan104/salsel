package com.salsel.service;

import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

public interface PdfGenerationService {
    byte[] generatePdf(String templateName, Model model, Long awbId);
    byte[] generateEmployeePdf(Long empId);
    Map<Long,List<byte[]>> generateBillingPdf(List<Map<String, Object>> billingMaps);

    byte[] generateSalassilStatementPdf();

}
