package com.salsel.controller;

import com.salsel.dto.PaginationResponse;
import com.salsel.dto.TicketCategoryDto;
import com.salsel.dto.TicketSubCategoryDto;
import com.salsel.service.TicketSubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketSubCategoryController {

    private final TicketSubCategoryService ticketSubCategoryService;

    public TicketSubCategoryController(TicketSubCategoryService ticketSubCategoryService) {
        this.ticketSubCategoryService = ticketSubCategoryService;
    }

    @PostMapping("/ticket-sub-category")
    @PreAuthorize("hasAuthority('CREATE_TICKET_SUB_CATEGORY') and hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<TicketSubCategoryDto> createTicketSubCategory(@RequestBody TicketSubCategoryDto ticketSubCategoryDto) {
        return ResponseEntity.ok(ticketSubCategoryService.save(ticketSubCategoryDto));
    }

    @GetMapping("/ticket-sub-category")
    @PreAuthorize("hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<List<TicketSubCategoryDto>> getAllTicketSubCategory(@RequestParam(value = "status") Boolean status) {
        List<TicketSubCategoryDto> ticketSubCategoryDtoList = ticketSubCategoryService.getAll(status);
        return ResponseEntity.ok(ticketSubCategoryDtoList);
    }

    @GetMapping("/ticket-sub-category/ticket-category/{id}")
    @PreAuthorize("hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<List<TicketSubCategoryDto>> getAllTicketSubCategoriesByTicketCategory(@PathVariable Long id) {
        List<TicketSubCategoryDto> ticketSubCategoryDtoList = ticketSubCategoryService.getAllByTicketCategory(id);
        return ResponseEntity.ok(ticketSubCategoryDtoList);
    }

    @GetMapping("/ticket-sub-category/page")
    @PreAuthorize("hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<PaginationResponse> getAllPaginatedDepartment(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {
        PaginationResponse paginationResponse = ticketSubCategoryService.getAllPaginatedTicketCategory(pageNumber, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/ticket-sub-category/{id}")
    @PreAuthorize("hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<TicketSubCategoryDto> getTicketSubCategoryById(@PathVariable Long id) {
        TicketSubCategoryDto ticketSubCategoryDto = ticketSubCategoryService.findById(id);
        return ResponseEntity.ok(ticketSubCategoryDto);
    }

    @GetMapping("/ticket-sub-category/name/{name}")
    @PreAuthorize("hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<TicketSubCategoryDto> getTicketSubCategoryByName(@PathVariable String name) {
        TicketSubCategoryDto ticketSubCategoryDto = ticketSubCategoryService.findByName(name);
        return ResponseEntity.ok(ticketSubCategoryDto);
    }

    @DeleteMapping("/ticket-sub-category/{id}")
    @PreAuthorize("hasAuthority('DELETE_TICKET_SUB_CATEGORY') and hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<Void> deleteTicketSubCategory(@PathVariable Long id) {
        ticketSubCategoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ticket-sub-category/{id}")
    @PreAuthorize("hasAuthority('CREATE_TICKET_SUB_CATEGORY') and hasAuthority('READ_TICKET_SUB_CATEGORY')")
    public ResponseEntity<TicketSubCategoryDto> updateTicketCategory(@PathVariable Long id,@RequestBody TicketSubCategoryDto ticketSubCategoryDto) {
        TicketSubCategoryDto updatedTicketSubCategoryDto = ticketSubCategoryService.update(id, ticketSubCategoryDto);
        return ResponseEntity.ok(updatedTicketSubCategoryDto);
    }

    @PutMapping("/ticket-sub-category/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateTicketSubCategoryStatusToActive(@PathVariable Long id) {
        ticketSubCategoryService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }
}
