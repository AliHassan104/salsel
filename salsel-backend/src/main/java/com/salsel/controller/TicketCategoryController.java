package com.salsel.controller;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.dto.TicketCategoryDto;
import com.salsel.service.TicketCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketCategoryController {
    private final TicketCategoryService ticketCategoryService;

    public TicketCategoryController(TicketCategoryService ticketCategoryService) {
        this.ticketCategoryService = ticketCategoryService;
    }

    @PostMapping("/ticket-category")
    @PreAuthorize("hasAuthority('CREATE_TICKET_CATEGORY') and hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<TicketCategoryDto> createTicketCategory(@RequestBody TicketCategoryDto ticketCategoryDto) {
        return ResponseEntity.ok(ticketCategoryService.save(ticketCategoryDto));
    }

    @GetMapping("/ticket-category")
    @PreAuthorize("hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<List<TicketCategoryDto>> getAllTicketCategory(@RequestParam(value = "status") Boolean status) {
        List<TicketCategoryDto> ticketCategoryDtoList = ticketCategoryService.getAll(status);
        return ResponseEntity.ok(ticketCategoryDtoList);
    }

    @GetMapping("/ticket-category/department-category/{id}")
    @PreAuthorize("hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<List<TicketCategoryDto>> getAllTicketCategoriesByDepartmentCategory(@PathVariable Long id) {
        List<TicketCategoryDto> ticketCategoryDtoList = ticketCategoryService.getAllByDepartmentCategory(id);
        return ResponseEntity.ok(ticketCategoryDtoList);
    }

    @GetMapping("/ticket-category/page")
    @PreAuthorize("hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<PaginationResponse> getAllPaginatedDepartment(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {
        PaginationResponse paginationResponse = ticketCategoryService.getAllPaginatedTicketCategory(pageNumber, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/ticket-category/{id}")
    @PreAuthorize("hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<TicketCategoryDto> getTicketCategoryById(@PathVariable Long id) {
        TicketCategoryDto ticketCategoryDto = ticketCategoryService.findById(id);
        return ResponseEntity.ok(ticketCategoryDto);
    }

    @GetMapping("/ticket-category/name/{name}")
    @PreAuthorize("hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<TicketCategoryDto> getTicketCategoryByName(@PathVariable String name) {
        TicketCategoryDto ticketCategoryDto = ticketCategoryService.findByName(name);
        return ResponseEntity.ok(ticketCategoryDto);
    }

    @DeleteMapping("/ticket-category/{id}")
    @PreAuthorize("hasAuthority('DELETE_TICKET_CATEGORY') and hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<Void> deleteTicketCategory(@PathVariable Long id) {
        ticketCategoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ticket-category/{id}")
    @PreAuthorize("hasAuthority('CREATE_TICKET_CATEGORY') and hasAuthority('READ_TICKET_CATEGORY')")
    public ResponseEntity<TicketCategoryDto> updateTicketCategory(@PathVariable Long id,@RequestBody TicketCategoryDto ticketCategoryDto) {
        TicketCategoryDto updatedTicketCategoryDto = ticketCategoryService.update(id, ticketCategoryDto);
        return ResponseEntity.ok(updatedTicketCategoryDto);
    }

    @PutMapping("/ticket-category/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateTicketCategoryStatusToActive(@PathVariable Long id) {
        ticketCategoryService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }
}
