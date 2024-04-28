package com.salsel.service.impl;

import com.lowagie.text.DocumentException;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Employee;
import com.salsel.repository.EmployeeRepository;
import com.salsel.service.PdfGenerationService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService {
    private final TemplateEngine templateEngine;
    private final EmployeeRepository employeeRepository;

    public PdfGenerationServiceImpl(TemplateEngine templateEngine, EmployeeRepository employeeRepository) {
        this.templateEngine = templateEngine;
        this.employeeRepository = employeeRepository;
    }
    @Override
    public byte[] generatePdf(String templateName, Model model, Long awbId) {
        try {
            // Convert Model to Thymeleaf Context
            Context context = new Context();
            model.asMap().forEach(context::setVariable);

            String htmlContent = templateEngine.process(templateName, context);
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

    @Override
    public byte[] generateEmployeePdf(Long empId) {
        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Employee not found for id => %d", empId)));

        String htmlContent = "<html><head><title>Employee Details</title></head><body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>" +
                "<div style='max-width: 800px; margin: 0 auto; padding: 10px 20px;'>" +
                "<div style='position: relative;'>" +
                "<img src='src/main/resources/static/images/logo.jpeg' style='position: absolute; top: -15px; left: 5px; width: 100px; height: auto;'/>" +
                "<h2 style='text-align: center;'>Employee #" + employee.getId() + "</h2>" +
                "<div style='margin-left: 20px; margin-top:40px;'>" +
                "<p><strong>Created At:</strong> " + (employee.getCreatedAt() != null ? employee.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "null") + "</p>" +
                "<p><strong>Name:</strong> " + (employee.getName() != null ? employee.getName() : "null") + "</p>" +
                "<p><strong>Phone:</strong> " + (employee.getPhone() != null ? employee.getPhone() : "null") + "</p>" +
                "<p><strong>Email:</strong> " + (employee.getEmail() != null ? employee.getEmail() : "null") + "</p>" +
                "<p><strong>Country:</strong> " + (employee.getCountry() != null ? employee.getCountry() : "null") + "</p>" +
                "<p><strong>City:</strong> " + (employee.getCity() != null ? employee.getCity() : "null") + "</p>" +
                "<p><strong>Address:</strong> " + (employee.getAddress() != null ? employee.getAddress() : "null") + "</p>" +
                "<p><strong>Nationality:</strong> " + (employee.getNationality() != null ? employee.getNationality() : "null") + "</p>" +
                "<p><strong>Job Title:</strong> " + (employee.getJobTitle() != null ? employee.getJobTitle() : "null") + "</p>" +
                "<p><strong>Department:</strong> " + (employee.getDepartment() != null ? employee.getDepartment() : "null") + "</p>" +
                "<p><strong>Salary:</strong> " + (employee.getSalary() != null ? employee.getSalary() : "null") + "</p>" +
                "<p><strong>Housing Allowance:</strong> " + (employee.getHousing() != null ? employee.getHousing() : "null") + "</p>" +
                "<p><strong>Transportation Allowance:</strong> " + (employee.getTransportation() != null ? employee.getTransportation() : "null") + "</p>" +
                "<p><strong>Mobile Allowance:</strong> " + (employee.getMobile() != null ? employee.getMobile() : "null") + "</p>" +
                "<p><strong>Other Allowance:</strong> " + (employee.getOtherAllowance() != null ? employee.getOtherAllowance() : "null") + "</p>" +
                "<p><strong>Total Amount:</strong> " + (employee.getTotalAmount() != null ? employee.getTotalAmount() : "null") + "</p>" +
                "<p><strong>Position:</strong> " + (employee.getPosition() != null ? employee.getPosition() : "null") + "</p>" +
                "<p><strong>Status:</strong> " + (employee.getStatus() != null ? employee.getStatus() : "null") + "</p>" +
                "</div></div></div></body></html>";


        // Step 2: Use Flying Saucer to convert HTML to PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();

        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }
    }



}
