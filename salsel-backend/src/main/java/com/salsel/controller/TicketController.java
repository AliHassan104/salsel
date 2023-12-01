package com.salsel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salsel.constants.TicketConstant;
import com.salsel.criteria.SearchCriteria;
import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.PageableRequest;
import com.salsel.dto.TicketDto;
import com.salsel.model.Ticket;
import com.salsel.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/ticket")

public class TicketController {

    @Autowired
    private TicketService ticketService;


    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<TicketDto> add(@RequestBody TicketDto ticketDto) {
        return ResponseEntity.ok(ticketService.save(ticketDto));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("")
    public ResponseEntity<Page<Ticket>> getAll(@RequestParam("search") String search,
                                               @RequestParam(value = "page") int page,
                                               @RequestParam(value = "size") int size,
                                               @RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<Ticket> departmentDtos = ticketService.findAll(searchCriteria, PageRequest.of(page, size,  Sort.by(sort).descending()));
        return ResponseEntity.ok(departmentDtos);
    }


    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getById(@PathVariable Long id){
        TicketDto ticketDto = ticketService.findById(id);
        return  ResponseEntity.ok(ticketDto);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> update(@RequestBody TicketDto ticketDto , @PathVariable Long id) throws Exception {
            return ResponseEntity.ok(ticketService.update(ticketDto , id));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        ticketService.delete(id);
        return ResponseEntity.ok().build();
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("/page")
    public ResponseEntity<Page<TicketDto>> getByPage(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                                               @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize){
        Page<TicketDto> ticketDtos = ticketService.findByPage(pageNumber,pageSize);
        return ResponseEntity.ok(ticketDtos);
    }

}
