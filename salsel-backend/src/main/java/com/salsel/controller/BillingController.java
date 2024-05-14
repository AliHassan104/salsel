package com.salsel.controller;

import com.salsel.dto.AccountDto;
import com.salsel.dto.BillingDto;
import com.salsel.service.BillingService;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.PdfGenerationService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.salsel.constants.ExcelConstants.ACCOUNT_TYPE;

@RestController
@RequestMapping("/api")
public class BillingController {

    private final BillingService billingService;
    private final ExcelGenerationService excelGenerationService;

    private final PdfGenerationService pdfGenerationService;

    public BillingController(BillingService billingService, ExcelGenerationService excelGenerationService, PdfGenerationService pdfGenerationService) {
        this.billingService = billingService;
        this.excelGenerationService = excelGenerationService;
        this.pdfGenerationService = pdfGenerationService;
    }

    @PostMapping("/billing")
    @PreAuthorize("hasAuthority('CREATE_BILLING') and hasAuthority('READ_BILLING')")
    public ResponseEntity<BillingDto> createBilling(@RequestBody BillingDto billingDto){
        return ResponseEntity.ok(billingService.save(billingDto));
    }

    @GetMapping("/billing")
    @PreAuthorize("hasAuthority('READ_BILLING')")
    public ResponseEntity<List<BillingDto>> getAllBillings(@RequestParam(value = "status") Boolean status){
        List<BillingDto> billingDtoList = billingService.getAll(status);
        return ResponseEntity.ok(billingDtoList);
    }

    @PutMapping("/billing/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_BILLING') and hasAuthority('READ_BILLING')")
    public ResponseEntity<Void> updateAccountStatusToActive(@PathVariable Long id) {
        billingService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/billing/{id}")
    @PreAuthorize("hasAuthority('CREATE_BILLING') and hasAuthority('READ_BILLING')")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        billingService.deleteById(id);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/billings")
//    @PreAuthorize("hasAuthority('READ_BILLING')")
//    public ResponseEntity<Map<Long, List<BillingDto>>> getAllBillingsGroupedByInvoice(@RequestParam(value = "status") Boolean status) {
//        Map<Long, List<BillingDto>> billingGroupedByInvoice = billingService.getAllGroupedByInvoice(status);
//        return ResponseEntity.ok(billingGroupedByInvoice);
//    }

    @GetMapping("/billing/{id}")
    @PreAuthorize("hasAuthority('READ_BILLING')")
    public ResponseEntity<BillingDto> getInvoiceById(@PathVariable Long id) {
        BillingDto billingDto = billingService.findById(id);
        return ResponseEntity.ok(billingDto);
    }



    @GetMapping("/download-billing-xl")
    public void downloadAccountsBetweenDates(HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=accounts.xlsx");

        // Get the OutputStream from the response
        OutputStream outputStream = response.getOutputStream();

        // Generate the billing report Excel data
        ByteArrayOutputStream excelData = excelGenerationService.generateBillingReport();

        // Write the generated Excel data to the response OutputStream
        excelData.writeTo(outputStream);

        // Close the OutputStream
        outputStream.close();
    }


    @GetMapping("/download-billing")
    public ResponseEntity<byte[]> generateEmployeePdf() {
        // Generate PDF
        byte[] pdfBytes = pdfGenerationService.generateBillingPdf();

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "EmployeeDetails.pdf");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
