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
                "<div style='max-width: 800px; margin: 0 auto; padding: 0px 20px;'>" +
                "<div style='position: relative;'>" +
                "<img src='src/main/resources/static/images/logo.jpeg' style='position: absolute; top: 0px; left: 5px; width: 120px; height: auto;'/>" +
                "<p style='position: absolute; top: 0px; right: 0px;color:#93003c; font-weight:bold;'>" + employee.getEmployeeNumber() + "</p>" +
                "<h2 style='text-align: center;'>Employee Details" + "</h2>" +
                "<div style='margin-left: 20px;'>";

// Personal Info Heading
        htmlContent += "<div style='display:flex; flex-wrap:wrap'>";
        htmlContent += "<div style='flex: 0 0 auto; width:50%;'>";
        htmlContent += "<h2 style='margin-top: 20px; color:#93003c;margin-top:50px'>Personal Information</h2>";
        htmlContent += "<div style='display: flex; flex-wrap: wrap;'>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Name:</strong> " + (employee.getName() != null ? employee.getName() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Nationality:</strong> " + (employee.getNationality() != null ? employee.getNationality() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Mobile:</strong> " + (employee.getPhone() != null ? employee.getPhone() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 100%;'><p><strong>Address:</strong> " + (employee.getAddress() != null ? employee.getAddress() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Country:</strong> " + (employee.getCountry() != null ? employee.getCountry() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>City:</strong> " + (employee.getCity() != null ? employee.getCity() : "null") + "</p></div>";
        htmlContent += "</div>"; // End of Personal Info Div
        htmlContent += "</div>"; // End of Personal Info Column

// Job Info Heading
        htmlContent += "<div style='flex: 0 0 auto; width:50%'>";
        htmlContent += "<h2 style='margin-top: 20px;  color:#93003c'>Job Information</h2>";
        htmlContent += "<div style='display: flex; flex-wrap: wrap;'>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Employee Number:</strong> " + (employee.getEmployeeNumber() != null ? employee.getEmployeeNumber() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Department:</strong> " + (employee.getDepartment() != null ? employee.getDepartment() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Job Title:</strong> " + (employee.getJobTitle() != null ? employee.getJobTitle() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Salary:</strong> " + (employee.getSalary() != null ? employee.getSalary() : "null") + "</p></div>";
        htmlContent += "</div>";
        htmlContent += "</div>"; // End of Personal Info Column
        htmlContent += "</div>";

//  Benefits and Allowances
        htmlContent += "<div style='flex-basis: 50%;'>";
        htmlContent += "<h2 style='margin-top: 20px;  color:#93003c'>Benefits and Allowances</h2>";
        htmlContent += "<div style='display: flex; flex-wrap: wrap;'>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Transportation Allowance:</strong> " + (employee.getTransportation() != null ? employee.getTransportation() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Other Allowance:</strong> " + (employee.getOtherAllowance() != null ? employee.getOtherAllowance() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Housing Allowance:</strong> " + (employee.getHousing() != null ? employee.getHousing() : "null") + "</p></div>";
        htmlContent += "<div style='flex-basis: 50%; padding-right: 20px;'><p><strong>Total Amount:</strong> " + (employee.getTotalAmount() != null ? employee.getTotalAmount() : "null") + "</p></div>";
        htmlContent += "</div>";
        htmlContent += "</div>";

        htmlContent += "</div></div></div></body></html>";



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
