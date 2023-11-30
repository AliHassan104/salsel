package com.salsel.controller;

import com.salsel.dto.TicketDto;
import com.salsel.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/ticket")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TicketDto>> getAllTicket() {
        List<TicketDto> ticketDtoList = ticketService.getAll();
        return ResponseEntity.ok(ticketDtoList);
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
