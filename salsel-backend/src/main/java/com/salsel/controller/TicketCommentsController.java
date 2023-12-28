package com.salsel.controller;

import com.salsel.dto.TicketCommentsDto;
import com.salsel.service.TicketCommentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketCommentsController {
    private final TicketCommentsService ticketCommentsService;

    public TicketCommentsController(TicketCommentsService ticketCommentsService) {
        this.ticketCommentsService = ticketCommentsService;
    }

    @PostMapping("/ticket-comments")
    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<TicketCommentsDto> createTicket(@RequestBody TicketCommentsDto ticketCommentsDto) {
        return ResponseEntity.ok(ticketCommentsService.save(ticketCommentsDto));
    }

    @GetMapping("/ticket-comments")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<List<TicketCommentsDto>> getAllTicketComments(@RequestParam(value = "status") Boolean status){
        List<TicketCommentsDto> ticketCommentsDtoList = ticketCommentsService.getAll(status);
        return ResponseEntity.ok(ticketCommentsDtoList);
    }

    @GetMapping("/ticket-comments/{id}")
    @PreAuthorize("hasAuthority('READ_TICKET')")
    public ResponseEntity<TicketCommentsDto> getTicketCommentsById(@PathVariable Long id) {
        TicketCommentsDto ticketCommentsDto = ticketCommentsService.findById(id);
        return ResponseEntity.ok(ticketCommentsDto);
    }

    @DeleteMapping("/ticket-comments/{id}")
    @PreAuthorize("hasAuthority('DELETE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<Void> deleteTicketComments(@PathVariable Long id) {
        ticketCommentsService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ticket-comments/{id}")
    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<TicketCommentsDto> updateTicketComments(@PathVariable Long id,@RequestBody TicketCommentsDto ticketCommentsDto) {
        TicketCommentsDto updatedTicketCommentsDto = ticketCommentsService.update(id, ticketCommentsDto);
        return ResponseEntity.ok(updatedTicketCommentsDto);
    }

    @PutMapping("/ticket-comments/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_TICKET') and hasAuthority('READ_TICKET')")
    public ResponseEntity<Void> updateTicketCommentsStatusToActive(@PathVariable Long id) {
        ticketCommentsService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }
}
