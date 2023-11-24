package com.salsel.controller;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.DepartmentDto;
import com.salsel.dto.TicketDto;
import com.salsel.model.DepartmentCategory;
import com.salsel.service.DepartmentCategoryService;
import com.salsel.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/department-category")
public class DepartmentCategoryController {

    @Autowired
    private DepartmentCategoryService departmentCategoryService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<DepartmentCategoryDto> add(@RequestBody DepartmentCategoryDto departmentCategoryDto) {
        return ResponseEntity.ok(departmentCategoryService.save(departmentCategoryDto));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("")
    public ResponseEntity<List<DepartmentCategoryDto>> getAll() {
        List<DepartmentCategoryDto> departmentDtos = departmentCategoryService.findAll();
        return ResponseEntity.ok(departmentDtos);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentCategoryDto> getById(@PathVariable Long id){
        DepartmentCategoryDto departmentCategoryDto = departmentCategoryService.findById(id);
        return  ResponseEntity.ok(departmentCategoryDto);
    }


    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentCategoryDto> update(@RequestBody DepartmentCategoryDto departmentCategoryDto , @PathVariable Long id) throws Exception {
        return ResponseEntity.ok(departmentCategoryService.update(departmentCategoryDto , id));
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        departmentCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER') or hasRole('ROLE_WORKER')")
    @GetMapping("/page")
    public ResponseEntity<List<DepartmentCategoryDto>> getByPage(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                                                     @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize){
        List<DepartmentCategoryDto> departmentCategoryDtos = departmentCategoryService.findByPage(pageNumber,pageSize);
        return ResponseEntity.ok(departmentCategoryDtos);
    }

}
