package com.salsel.controller;

import com.salsel.dto.AwbDto;
import com.salsel.service.AwbService;
import com.salsel.service.ExcelGenerationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.salsel.constants.AwbStatusConstants.DELIVERED;
import static com.salsel.constants.AwbStatusConstants.PICKED_UP;
import static com.salsel.constants.ExcelConstants.AWB_TYPE;
import static com.salsel.constants.ExcelConstants.TRANSIT;

@RestController
@RequestMapping("/api")
public class AwbController {
    private final AwbService awbService;
    private final ExcelGenerationService excelGenerationService;

    public AwbController(AwbService awbService, ExcelGenerationService excelGenerationService) {
        this.awbService = awbService;
        this.excelGenerationService = excelGenerationService;
    }

    @PostMapping("/awb")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> createAwb(@RequestBody AwbDto awbDto) {
        return ResponseEntity.ok(awbService.save(awbDto));
    }

    @GetMapping("/awb")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<List<AwbDto>> getAllAwb(@RequestParam(value = "status") Boolean status) {
        List<AwbDto> awbDtoList = awbService.getAll(status);
        return ResponseEntity.ok(awbDtoList);
    }

    @GetMapping("/awb/logged-in-user")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<List<AwbDto>> getAllAwbByLoggedInUser(@RequestParam(value = "status") Boolean status) {
        List<AwbDto> awbDtoList = awbService.getAwbByLoggedInUser(status);
        return ResponseEntity.ok(awbDtoList);
    }

    @GetMapping("/awb/assigned-user")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<List<AwbDto>> getAllAwbByAssignedUser(@RequestParam(value = "status") Boolean status,
                                                                @RequestParam(value = "user") String user) {
        List<AwbDto> awbDtoList = awbService.getAwbByAssignedUser(user,status);
        return ResponseEntity.ok(awbDtoList);
    }

    @GetMapping("/awb/logged-in-user-role")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<List<AwbDto>> getAllAwbByLoggedInUserRole(@RequestParam(value = "status") Boolean status) {
        List<AwbDto> awbDtoList = awbService.getAwbByLoggedInUserRole(status);
        return ResponseEntity.ok(awbDtoList);
    }

    @GetMapping("/awb/logged-in-user-and-role")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<List<AwbDto>> getAllAwbByLoggedInUserAndRole(@RequestParam(value = "status") Boolean status) {
        List<AwbDto> awbDtoList = awbService.getAwbByLoggedInUserAndRole(status);
        return ResponseEntity.ok(awbDtoList);
    }

    @GetMapping("/awb/pdf/{file-name}/{awbId}")
    public ResponseEntity<byte[]> getAwbPdf(@PathVariable(name = "file-name") String fileName,
                                            @PathVariable(name = "awbId") Long awbId) {
        byte[] pdf = awbService.downloadAwbPdf(fileName, awbId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }


    @GetMapping("/awb/{id}")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> getAwbById(@PathVariable Long id) {
        AwbDto awbDto = awbService.findById(id);
        return ResponseEntity.ok(awbDto);
    }

    @GetMapping("/awb/unique-number/{uniqueNumber}")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> getAwbByUniqueNumber(@PathVariable Long uniqueNumber) {
        AwbDto awbDto = awbService.findByUniqueNumber(uniqueNumber);
        return ResponseEntity.ok(awbDto);
    }

    @DeleteMapping("/awb/{id}")
    @PreAuthorize("hasAuthority('DELETE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<Void> deleteAwb(@PathVariable Long id) {
        awbService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/awb/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateAwbStatusToActive(@PathVariable Long id) {
        awbService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/awb/{id}")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> updateAwb(@PathVariable Long id, @RequestBody AwbDto awbDto) {
        AwbDto updatedAwbDto = awbService.update(id, awbDto);
        return ResponseEntity.ok(updatedAwbDto);
    }

    @PutMapping("/awb/awb-status/{status}/unique-number/{uniqueNumber}")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> updateAwbStatusOnScan(@PathVariable String status, @PathVariable Long uniqueNumber) {
        AwbDto updatedAwbDto = awbService.updateAwbStatusOnScan(uniqueNumber,status);
        return ResponseEntity.ok(updatedAwbDto);
    }

    @PutMapping("/awb/{awb-id}/user/{user-id}")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> assignAwbToUser(@PathVariable(name = "awb-id") Long awbId,
                                                  @PathVariable(name = "user-id") Long userId) {
        AwbDto assignedAwbDto = awbService.assignAwbToUser(userId,awbId);
        return ResponseEntity.ok(assignedAwbDto);
    }

    @PutMapping("/awb/awb-status/scan")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<List<AwbDto>> updateMultipleAwbStatusOnScan(@RequestBody Map<Long, String> statusMap) {
        List<AwbDto> updatedAwbDtoList = awbService.updateMultipleAwbStatusOnScan(statusMap);
        return ResponseEntity.ok(updatedAwbDtoList);
    }

    @PutMapping("/awb/awb-status/unique-number/comment")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> updateAwbStatusAndComment(@RequestParam() String status,
                                                            @RequestParam() Long uniqueNumber,
                                                            @RequestParam() String comment) {
        AwbDto updatedAwbDto = awbService.updateAwbStatusAndCommentOnScan(uniqueNumber,status,comment);
        return ResponseEntity.ok(updatedAwbDto);
    }

    @GetMapping("/download-awb-excel")
    public void downloadAwbBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=airbills.xlsx");

        List<AwbDto> awbDtoList = awbService.getAwbBetweenDates(startDate, endDate);
        List<Map<String, Object>> excelData = excelGenerationService.convertAirBillsToExcelData(awbDtoList);

        OutputStream outputStream = response.getOutputStream();
        excelGenerationService.createExcelFile(excelData, outputStream, AWB_TYPE);
        outputStream.close();
    }

    @GetMapping("/awb/awb-status-counts")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<Map<String, Long>> getAwbStatusCounts() {
        Map<String, Long> statusCounts = awbService.getAwbStatusCounts();
        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping("/awb/status-counts")
    public ResponseEntity<Map<String, Long>> getStatusCounts() {
        Map<String, Long> statusCounts = awbService.getStatusCounts();
        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping("/awb/logged-in-user-awb-status-counts")
    public ResponseEntity<Map<String, Long>> getAwbStatusCountsBasedOnLoggedInUser() {
        Map<String, Long> statusCounts = awbService.getAwbStatusCountsBasedOnLoggedInUser();
        return ResponseEntity.ok(statusCounts);
    }


    @GetMapping("/awb/logged-in-user-status-counts")
    public ResponseEntity<Map<String, Long>> getStatusCountsBasedOnLoggedInUser() {
        Map<String, Long> statusCounts = awbService.getStatusCountsBasedOnLoggedInUser();
        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping("/awb/count/assigned-user")
    public ResponseEntity<Long> getAwbCountsBasedOnAssignedUser() {
        Long counts = awbService.getAllAwbByAssignedUser();
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/awb/created-at-range")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<Map<String, LocalDate>> getTicketCreatedAtRange() {
        Map<String, LocalDate> dateRange = new HashMap<>();

        LocalDate minDate = awbService.getMinCreatedAt();
        LocalDate maxDate = awbService.getMaxCreatedAt();

        dateRange.put("minDate", minDate);
        dateRange.put("maxDate", maxDate);

        return ResponseEntity.ok(dateRange);
    }

//    @GetMapping("/awb/status-report")
//    public ResponseEntity<List<Map<String, Object>>> getAllAwbByStatusUpdateInLastDay(@RequestParam() String status){
//        List<Map<String,Object>> map = awbService.getAwbByStatusChangedOnPreviousDay(status);
//        return ResponseEntity.ok(map);
//    }

//    @GetMapping("/awb/transit-status-report")
//    public ResponseEntity<List<Map<String, Object>>> getAllAwbByStatusUpdateInLastDayForTransitReport(){
//        List<Map<String,Object>> map = awbService.getAwbByStatusChangedLastDayExcludingPickedUpAndDelivered();
//        return ResponseEntity.ok(map);
//    }

    @GetMapping("/awb/status-report/{status}")
    public void StatusReport(@PathVariable String status,
                                        HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");

        List<Map<String,Object>> map = awbService.getAwbByStatusChangedOnPreviousDay(status);
        OutputStream outputStream = response.getOutputStream();
        excelGenerationService.createExcelFile(map, outputStream, PICKED_UP);
        outputStream.close();
    }

    @GetMapping("/awb/transit-status-report")
    public void TransitStatusReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Report.xlsx");

        List<Map<String,Object>> map = awbService.getAwbByStatusChangedLastDayExcludingPickedUpAndDelivered();
        OutputStream outputStream = response.getOutputStream();
        excelGenerationService.createExcelFile(map, outputStream, TRANSIT);
        outputStream.close();
    }
}
