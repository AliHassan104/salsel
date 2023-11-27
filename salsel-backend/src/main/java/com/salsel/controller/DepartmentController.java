package com.salsel.controller;


import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.DepartmentDto;
import com.salsel.dto.TicketDto;
import com.salsel.model.Department;
import com.salsel.service.DepartmentService;
import com.salsel.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<DepartmentDto> add(@RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(departmentService.save(departmentDto));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("")
    public ResponseEntity<List<DepartmentDto>> getAll(){
        List<DepartmentDto> departmentDtos = departmentService.findAll();
        return ResponseEntity.ok(departmentDtos);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getById(@PathVariable Long id){
        DepartmentDto departmentDto = departmentService.findById(id);
        return  ResponseEntity.ok(departmentDto);
    }

    //  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> update(@RequestBody DepartmentDto departmentDto , @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(departmentService.update(departmentDto , id));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        departmentService.delete(id);
        return ResponseEntity.ok().build();
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("/page")
    public ResponseEntity<Page<DepartmentDto>> getByPage(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                                                         @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize){
        Page<DepartmentDto> departmentDtos = departmentService.findByPage(pageNumber,pageSize);
        return ResponseEntity.ok(departmentDtos);
    }

}
