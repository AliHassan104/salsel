package com.salsel.controller;

import com.salsel.dto.ServiceTypeDto;
import com.salsel.service.ServiceTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @PostMapping("/service-type")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceTypeDto> createServiceType(@RequestBody ServiceTypeDto serviceTypeDto) {
        return ResponseEntity.ok(serviceTypeService.save(serviceTypeDto));
    }

    @GetMapping("/service-type")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ServiceTypeDto>> getAllServiceType() {
        List<ServiceTypeDto> serviceTypeDtoList = serviceTypeService.getAll();
        return ResponseEntity.ok(serviceTypeDtoList);
    }

    @GetMapping("/service-type/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceTypeDto> getServiceTypeById(@PathVariable Long id) {
        ServiceTypeDto serviceTypeDto = serviceTypeService.findById(id);
        return ResponseEntity.ok(serviceTypeDto);
    }

    @GetMapping("/service-type/code/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceTypeDto> getServiceTypeByCode(@PathVariable String code) {
        ServiceTypeDto serviceTypeDto = serviceTypeService.findByCode(code);
        return ResponseEntity.ok(serviceTypeDto);
    }

    @DeleteMapping("/service-type/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteServiceType(@PathVariable Long id) {
        serviceTypeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/service-type/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceTypeDto> updateServiceType(@PathVariable Long id, @RequestBody ServiceTypeDto serviceTypeDto) {
        ServiceTypeDto updatedServiceTypeDto = serviceTypeService.update(id, serviceTypeDto);
        return ResponseEntity.ok(updatedServiceTypeDto);
    }
}
