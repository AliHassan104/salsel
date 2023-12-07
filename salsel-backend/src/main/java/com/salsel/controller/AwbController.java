package com.salsel.controller;

import com.salsel.dto.AwbDto;
import com.salsel.service.AwbService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Testing
@RestController
@RequestMapping("/api")
public class AwbController {
    private final AwbService awbService;

    public AwbController(AwbService awbService) {
        this.awbService = awbService;
    }

    @PostMapping("/awb")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AwbDto> createAccount(@RequestBody AwbDto awbDto) {
        return ResponseEntity.ok(awbService.save(awbDto));
    }

    @GetMapping("/awb")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AwbDto>> getAllAwb(@RequestParam(value = "status") Boolean status) {
        List<AwbDto> awbDtoList = awbService.getAll(status);
        return ResponseEntity.ok(awbDtoList);
    }

    @GetMapping("/awb/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AwbDto> getAwbById(@PathVariable Long id) {
        AwbDto awbDto = awbService.findById(id);
        return ResponseEntity.ok(awbDto);
    }

    @DeleteMapping("/awb/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAwb(@PathVariable Long id) {
        awbService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/awb/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateAwbStatusToActive(@PathVariable Long id) {
        awbService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/awb/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AwbDto> updateAwb(@PathVariable Long id, @RequestBody AwbDto awbDto) {
        AwbDto updatedAwbDto = awbService.update(id, awbDto);
        return ResponseEntity.ok(updatedAwbDto);
    }
}
