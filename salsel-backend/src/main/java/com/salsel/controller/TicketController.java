package com.salsel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.TicketDto;
import com.salsel.model.Ticket;
import com.salsel.service.TicketService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TicketController {
    private final TicketService ticketService;
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/ticket")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto) {
        return ResponseEntity.ok(ticketService.save(ticketDto));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("")
    public ResponseEntity<Page<Ticket>> getAllTickets(@RequestParam("search") String search,
                                               @RequestParam(value = "page") int page,
                                               @RequestParam(value = "size") int size,
                                               @RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonProcessingException, JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<Ticket> departmentDtos = ticketService.findAll(searchCriteria, PageRequest.of(page, size,  Sort.by(sort).descending()));
        return ResponseEntity.ok(departmentDtos);
    }

    @GetMapping("/ticket/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        TicketDto ticketDto = ticketService.findById(id);
        return ResponseEntity.ok(ticketDto);
    }

    @DeleteMapping("/ticket/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ticket/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable Long id,@RequestBody TicketDto ticketDto) {
        TicketDto updatedTicketDto = ticketService.update(id, ticketDto);
        return ResponseEntity.ok(updatedTicketDto);
    }
}
