package com.salsel.controller;

import com.salsel.dto.AddressBookDto;
import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.service.AwbShippingHistoryService;
import com.salsel.service.ExcelGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AwbShippingHistoryController {

    private final AwbShippingHistoryService awbShippingHistoryService;
    private final ExcelGenerationService excelGenerationService;

    public AwbShippingHistoryController(AwbShippingHistoryService awbShippingHistoryService, ExcelGenerationService excelGenerationService) {
        this.awbShippingHistoryService = awbShippingHistoryService;
        this.excelGenerationService = excelGenerationService;
    }

    @GetMapping("/awb-shipping-history")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity<List<AwbShippingHistoryDto>> getAllAwbShippingHistory(@RequestParam(value = "awbId") Long awbId) {
        List<AwbShippingHistoryDto> awbShippingHistoryDtoList = awbShippingHistoryService.findByAwbId(awbId);
        return ResponseEntity.ok(awbShippingHistoryDtoList);
    }

    @PutMapping("/awb-shipping-history/update-comment")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity<AwbShippingHistoryDto> updateCommentInAwbShippingHistory(@RequestParam(value = "awbId") Long awbId,
                                                                                   @RequestParam(value = "comment") String comment) {
        AwbShippingHistoryDto awbShippingHistoryDto = awbShippingHistoryService.addCommentToAwbShippingHistory(comment,awbId);
        return ResponseEntity.ok(awbShippingHistoryDto);
    }

    @GetMapping("/awb-shipping-history/awb/{id}")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity<AwbShippingHistoryDto> getAwbShippingHistoryByAwb(@PathVariable() Long id) {
        AwbShippingHistoryDto awbShippingHistoryDto = awbShippingHistoryService.findLatestAwbShippingHistoryByAwb(id);
        return ResponseEntity.ok(awbShippingHistoryDto);
    }

    @PostMapping("/awb-shipping-history/multiple-awb")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity <Map<Long, List<AwbShippingHistoryDto>>> getAwbShippingHistoryByMultipleAwb(@RequestBody List<Long> awbIds) {
        Map<Long, List<AwbShippingHistoryDto>> awbShippingHistoryDto = awbShippingHistoryService.findShippingByAwbIds(awbIds);
        return ResponseEntity.ok(awbShippingHistoryDto);
    }

    @GetMapping("/awb-shipping-history/tracking-number/{tracking-number}")
    public ResponseEntity <List<AwbShippingHistoryDto>> getAwbTrackingHistoryByTrackingNumber(@PathVariable(value = "tracking-number") Long trackingNumber) {
        List<AwbShippingHistoryDto> awbShippingHistoryDto = awbShippingHistoryService.findAllAwbHistoryByAwbNumber(trackingNumber);
        return ResponseEntity.ok(awbShippingHistoryDto);
    }

    @GetMapping("/download-tracking-excel")
    public void downloadAccountsBetweenDates(@RequestParam List<Long> trackingNumbers, HttpServletResponse response) throws IOException {


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=accounts.xlsx");

        // Get the OutputStream from the response
        OutputStream outputStream = response.getOutputStream();

        // Generate the billing report Excel data
        ByteArrayOutputStream excelData = excelGenerationService.generateShipmentTrackingReport(awbShippingHistoryService.getAllShippingDataByExcel(trackingNumbers));

        // Write the generated Excel data to the response OutputStream
        excelData.writeTo(outputStream);

        // Close the OutputStream
        outputStream.close();
    }

    @GetMapping("/download-awb-history-excel")
    public void downloadAwbHistoryExcel(@RequestParam Long trackingNumber, HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=awbhistory.xlsx");

        // Get the OutputStream from the response
        OutputStream outputStream = response.getOutputStream();

        // Generate the billing report Excel data
        ByteArrayOutputStream excelData = excelGenerationService.generateAwbHistoryReport(awbShippingHistoryService.findAllAwbHistoryByAwbNumber(trackingNumber));

        // Write the generated Excel data to the response OutputStream
        excelData.writeTo(outputStream);

        // Close the OutputStream
        outputStream.close();
    }
}
