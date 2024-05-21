package com.salsel.controller;

import com.salsel.dto.BillingDto;
import com.salsel.model.BillingAttachment;
import com.salsel.service.BillingService;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.PdfGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<BillingAttachment>> createBilling(@RequestBody MultipartFile file){
        return ResponseEntity.ok(billingService.save(file));
    }

    @PostMapping("/upload-excel")
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

//    @GetMapping("/billings")
//    @PreAuthorize("hasAuthority('READ_BILLING')")
//    public ResponseEntity<Map<Long, List<BillingDto>>> getAllBillingsGroupedByInvoice(@RequestParam(value = "status") Boolean status) {
//        Map<Long, List<BillingDto>> billingGroupedByInvoice = billingService.getAllGroupedByInvoice(status);
//        return ResponseEntity.ok(billingGroupedByInvoice);
//    }

    @PostMapping("/download-billing-xl")
    public void downloadBillingExcel(@RequestBody List<BillingDto> billingDtoList,HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=billingExcel.xlsx");

        // Get the OutputStream from the response
        OutputStream outputStream = response.getOutputStream();

        // Generate the billing report Excel data
//        ByteArrayOutputStream excelData = excelGenerationService.generateBillingReport(billingDtoList);

        // Write the generated Excel data to the response OutputStream
//        excelData.writeTo(outputStream);

        // Close the OutputStream
        outputStream.close();
    }


//    @PostMapping("/download-billing")
//    public ResponseEntity<List<byte[]>> generateEmployeePdf(@RequestBody List<BillingDto> billingDtoList) {
//        // Generate PDF
//        List<byte[]> pdfBytes = pdfGenerationService.generateBillingPdf(billingDtoList);
//
//        // Set response headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentDispositionFormData("inline", "Billing.pdf");
//        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
//    }

//    @PostMapping("/download-billing")
//    public ResponseEntity<byte[]> generateBillingZip(@RequestBody List<BillingDto> billingDtoList) {
//        // Generate PDFs
//        List<byte[]> pdfBytesList = pdfGenerationService.generateBillingPdf(billingDtoList);
//
//        // Create a zip file containing all PDFs
//        byte[] zipBytes = createZipFile(pdfBytesList);
//
//        // Set response headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", "Billing.zip");
//        headers.setContentLength(zipBytes.length);
//
//        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
//    }
//
//    private byte[] createZipFile(List<byte[]> pdfBytesList) {
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             ZipOutputStream zos = new ZipOutputStream(baos)) {
//
//            for (int i = 0; i < pdfBytesList.size(); i++) {
//                byte[] pdfBytes = pdfBytesList.get(i);
//                ZipEntry zipEntry = new ZipEntry("Billing_" + (i + 1) + ".pdf");
//                zos.putNextEntry(zipEntry);
//                zos.write(pdfBytes);
//                zos.closeEntry();
//            }
//
//            zos.finish();
//            return baos.toByteArray();
//        } catch (IOException e) {
//            throw new RuntimeException("Error creating zip file", e);
//        }
//    }
}
