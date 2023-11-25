package com.salsel.controller;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.TicketDto;
import com.salsel.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<TicketDto>> getAll() {
        List<TicketDto> departmentDtos = ticketService.findAll();
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
