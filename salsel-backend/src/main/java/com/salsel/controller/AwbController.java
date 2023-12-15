package com.salsel.controller;

import com.salsel.dto.AwbDto;
import com.salsel.service.AwbService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AwbController {
    private final AwbService awbService;

    public AwbController(AwbService awbService) {
        this.awbService = awbService;
    }

    @PostMapping("/awb")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> createAwb(@RequestBody AwbDto awbDto) {
        return ResponseEntity.ok(awbService.save(awbDto));
    }

    @GetMapping("/awb")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<List<AwbDto>> getAllAwb() {
        List<AwbDto> awbDtoList = awbService.getAll();
        return ResponseEntity.ok(awbDtoList);
    }

    @GetMapping("/awb/pdf/{file-name}/{awbId}")
    public ResponseEntity<byte[]> getAwbPdf(@PathVariable(name = "file-name") String fileName,
                                            @PathVariable(name = "awbId") Long awbId) {
        byte[] pdf = awbService.downloadAwbPdf(fileName, awbId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName + ".pdf");

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }


    @GetMapping("/awb/{id}")
    @PreAuthorize("hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> getAwbById(@PathVariable Long id) {
        AwbDto awbDto = awbService.findById(id);
        return ResponseEntity.ok(awbDto);
    }

    @DeleteMapping("/awb/{id}")
    @PreAuthorize("hasAuthority('DELETE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<Void> deleteAwb(@PathVariable Long id) {
        awbService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/awb/{id}")
    @PreAuthorize("hasAuthority('CREATE_AWB') and hasAuthority('READ_AWB')")
    public ResponseEntity<AwbDto> updateAwb(@PathVariable Long id, @RequestBody AwbDto awbDto) {
        AwbDto updatedAwbDto = awbService.update(id, awbDto);
        return ResponseEntity.ok(updatedAwbDto);
    }
}
