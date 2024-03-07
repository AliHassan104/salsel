package com.salsel.controller;

import com.salsel.dto.AddressBookDto;
import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.service.AwbShippingHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AwbShippingHistoryController {

    private final AwbShippingHistoryService awbShippingHistoryService;

    public AwbShippingHistoryController(AwbShippingHistoryService awbShippingHistoryService) {
        this.awbShippingHistoryService = awbShippingHistoryService;
    }

    @GetMapping("/awb-shipping-history")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity<List<AwbShippingHistoryDto>> getAllAwbShippingHistory(@RequestParam(value = "awbId") Long awbId) {
        List<AwbShippingHistoryDto> awbShippingHistoryDtoList = awbShippingHistoryService.findByAwbId(awbId);
        return ResponseEntity.ok(awbShippingHistoryDtoList);
    }
}
