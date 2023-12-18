package com.salsel.service.impl;

import com.salsel.service.PdfGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
@Slf4j
public class PdfGenerationServiceImpl implements PdfGenerationService {
    private final TemplateEngine templateEngine;

    public PdfGenerationServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    @Override
    public byte[] generatePdf(String templateName, Model model, Long awbId) {
        try {
            // Convert Model to Thymeleaf Context
            Context context = new Context();
            model.asMap().forEach(context::setVariable);

            String htmlContent = templateEngine.process(templateName, context);
            log.info(htmlContent);
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(htmlContent);
                renderer.layout();
                renderer.createPDF(outputStream, false);
                renderer.finishPDF();
                return outputStream.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException("Error generating PDF from HTML: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }

}
