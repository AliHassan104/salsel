package com.salsel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.TicketDto;
import com.salsel.model.Ticket;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.salsel.constants.ExcelConstants.TICKET_TYPE;
import static com.salsel.constants.ExcelConstants.USER_TYPE;

@RestController
@RequestMapping("/api")
public class TicketController {
    private final TicketService ticketService;
    private final ExcelGenerationService excelGenerationService;

    public TicketController(TicketService ticketService, ExcelGenerationService excelGenerationService) {
        this.ticketService = ticketService;
        this.excelGenerationService = excelGenerationService;
    }

//    @PostMapping("/ticket")
//    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
//    public ResponseEntity<TicketDto> createTicket(@RequestPart TicketDto ticketDto,
//                                                  @RequestPart("file") MultipartFile file) {
//        return ResponseEntity.ok(ticketService.save(ticketDto, file));
//    }

    @PostMapping("/ticket")
    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<TicketDto> createTicket(@RequestPart("ticketDto") TicketDto ticketDto,
                                                  @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        return ResponseEntity.ok(ticketService.save(ticketDto, files));
    }


    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("")
    public ResponseEntity<Page<Ticket>> getAllTickets(@RequestParam("search") String search,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "size") int size,
                                                      @RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonProcessingException, JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<Ticket> departmentDtos = ticketService.findAll(searchCriteria, PageRequest.of(page, size, Sort.by(sort).descending()));
        return ResponseEntity.ok(departmentDtos);
    }

    @GetMapping("/ticket")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<List<TicketDto>> getAllTickets(@RequestParam(value = "status") Boolean status) {
        List<TicketDto> ticketDtoList = ticketService.getAll(status);
        return ResponseEntity.ok(ticketDtoList);
    }

    @GetMapping("/ticket/logged-in-user")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<List<TicketDto>> getAllTicketsCreatedByLoggedInUser(@RequestParam(value = "status") Boolean status) {
        List<TicketDto> ticketDtoList = ticketService.getTicketsByLoggedInUser(status);
        return ResponseEntity.ok(ticketDtoList);
    }

    @GetMapping("/ticket/logged-in-user-and-role")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<List<TicketDto>> getAllTicketsCreatedByLoggedInUserAndRole(@RequestParam(value = "status") Boolean status) {
        List<TicketDto> ticketDtoList = ticketService.getTicketsByLoggedInUserAndRole(status);
        return ResponseEntity.ok(ticketDtoList);
    }


    @GetMapping("/ticket/{id}")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        TicketDto ticketDto = ticketService.findById(id);
        return ResponseEntity.ok(ticketDto);
    }

    @DeleteMapping("/ticket/{id}")
    @PreAuthorize("hasAuthority('DELETE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/ticket/{id}/filename/{fileName}")
//    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
//    public ResponseEntity<TicketDto> updateTicket(@PathVariable Long id, @RequestPart TicketDto ticketDto,
//                                                  @RequestPart("file") MultipartFile file, @PathVariable String fileName) {
//        TicketDto updatedTicketDto = ticketService.update(id, ticketDto, file, fileName);
//        return ResponseEntity.ok(updatedTicketDto);
//    }

    @PutMapping("/ticket/{id}/filenames")
    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable Long id, @RequestPart("ticketDto") TicketDto ticketDto,
                                                  @RequestPart(name = "files", required = false) List<MultipartFile> files,
                                                  @RequestParam("fileNames") List<String> fileNames) {
        TicketDto updatedTicketDto = ticketService.update(id, ticketDto, files, fileNames);
        return ResponseEntity.ok(updatedTicketDto);
    }


    @PutMapping("/ticket/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<Void> updateTicketStatusToActive(@PathVariable Long id) {
        ticketService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download-ticket-excel")
    public void downloadTicketsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=tickets.xlsx");

        List<TicketDto> tickets = ticketService.getTicketsBetweenDates(startDate, endDate);
        List<Map<String, Object>> excelData = excelGenerationService.convertTicketsToExcelData(tickets);

        OutputStream outputStream = response.getOutputStream();
        excelGenerationService.createExcelFile(excelData, outputStream, TICKET_TYPE);
        outputStream.close();
    }

    @GetMapping("/ticket/created-at-range")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<Map<String, LocalDate>> getTicketCreatedAtRange() {
        Map<String, LocalDate> dateRange = new HashMap<>();

        LocalDate minDate = ticketService.getMinCreatedAt();
        LocalDate maxDate = ticketService.getMaxCreatedAt();

        dateRange.put("minDate", minDate);
        dateRange.put("maxDate", maxDate);

        return ResponseEntity.ok(dateRange);
    }

    @GetMapping("/ticket/logged-in-user-status-counts")
    public ResponseEntity<Map<String, Long>> getStatusCountsBasedOnLoggedInUser() {
        Map<String, Long> statusCounts = ticketService.getStatusCountsBasedOnLoggedInUser();
        return ResponseEntity.ok(statusCounts);
    }

    @GetMapping("/ticket/status-counts")
    public ResponseEntity<Map<String, Long>> getStatusCounts() {
        Map<String, Long> statusCounts = ticketService.getStatusCounts();
        return ResponseEntity.ok(statusCounts);
    }
}
