package com.salsel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsel.dto.TicketDto;
import com.salsel.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("")
    public ResponseEntity<List<TicketDto>> get(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                                                           @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize){
        List<TicketDto> ticketDtos = ticketService.findPage(pageNumber,pageSize);
        return ResponseEntity.ok(ticketDtos);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getById(@PathVariable Long id){
        TicketDto ticketDto = ticketService.findById(id);
        return  ResponseEntity.ok(ticketDto);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        ticketService.delete(id);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateAchievementById(@RequestBody TicketDto ticketDto , @PathVariable Long id) throws Exception {
            return ResponseEntity.ok(ticketService.update(ticketDto , id));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<TicketDto> addAchievements(@RequestBody TicketDto ticketDto) {

            return ResponseEntity.ok(ticketService.save(ticketDto));

    }



}
