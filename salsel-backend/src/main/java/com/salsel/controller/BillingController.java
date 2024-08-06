package com.salsel.controller;

import com.salsel.dto.BillingDto;
import com.salsel.model.BillingAttachment;
import com.salsel.service.BillingService;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.PdfGenerationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static com.salsel.constants.ExcelConstants.SALASSIL_STATEMENT;

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
    public ResponseEntity<List<BillingAttachment>> createBilling(@RequestPart("file") MultipartFile file){
        return ResponseEntity.ok(billingService.save(file));
    }

    @PostMapping("/billing/upload-excel")
    @PreAuthorize("hasAuthority('CREATE_BILLING') and hasAuthority('READ_BILLING')")
    public ResponseEntity<List<BillingDto>> uploadExcelFile(@RequestPart("file") MultipartFile file){
        List<BillingDto> billingList = billingService.uploadDataExcel(file);
        return ResponseEntity.ok(billingList);
    }

    @PostMapping("/billing/map-billings")
    @PreAuthorize("hasAuthority('CREATE_BILLING') and hasAuthority('READ_BILLING')")
    public ResponseEntity<List<Map<String, Object>>> billingMap(List<BillingDto> billingDtoList){
        List<Map<String, Object>> billingMap = billingService.getBillingInvoiceDataByExcelUploaded(billingDtoList);
        return ResponseEntity.ok(billingMap);
    }


    @GetMapping("/billing")
    @PreAuthorize("hasAuthority('READ_BILLING')")
    public ResponseEntity<List<BillingDto>> getAllBillings(@RequestParam(value = "status") Boolean status){
        List<BillingDto> billingDtoList = billingService.getAll(status);
        return ResponseEntity.ok(billingDtoList);
    }

    @PostMapping("/billing/sorted-employees-invoices")
    @PreAuthorize("hasAuthority('READ_BILLING')")
    public ResponseEntity<List<Map<String, Object>>> getAllBillings(@RequestBody List<BillingDto> billingDtoList){
        List<Map<String, Object>> billingMap = billingService.getBillingInvoiceDataByExcelUploaded(billingDtoList);
        return ResponseEntity.ok(billingMap);
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

    @GetMapping("/billing/resend-invoice/{id}")
    @PreAuthorize("hasAuthority('READ_BILLING')")
    public ResponseEntity<Void> resendInvoice(@PathVariable Long id){
        billingService.resendBillingInvoice(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/billing/salassil-statement")
    @PreAuthorize("hasAuthority('READ_BILLING')")
    public void getMultipleAwbStatusOnScanReport(HttpServletResponse response) throws IOException {

        List<Map<String, Object>> data = billingService.getSalaasilStatmentForAllTheInvoices();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Salassil_Statement.xlsx");

        OutputStream outputStream = response.getOutputStream();
        excelGenerationService.createExcelFile(data, outputStream, SALASSIL_STATEMENT);
        outputStream.close();
    }

    @GetMapping("/billing/download-invoice/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
        try {
            byte[] invoiceBytes = billingService.downloadBillingInvoice(id);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"invoice.pdf\"")
                    .body(invoiceBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error downloading invoice: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/billing/download-statement/{id}")
    public ResponseEntity<byte[]> downloadStatement(@PathVariable Long id) {
        try {
            byte[] statementBytes = billingService.downloadBillingStatement(id);

            return ResponseEntity.ok()
                    .contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"statement.xlsx\"")
                    .body(statementBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error downloading statement: " + e.getMessage()).getBytes());
        }
    }
}

