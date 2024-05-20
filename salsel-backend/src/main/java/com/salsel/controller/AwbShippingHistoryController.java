package com.salsel.controller;

import com.salsel.dto.AddressBookDto;
import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.service.AwbShippingHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/awb-shipping-history/update-comment")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity<AwbShippingHistoryDto> updateCommentInAwbShippingHistory(@RequestParam(value = "awbId") Long awbId,
                                                                                   @RequestParam(value = "comment") String comment) {
        AwbShippingHistoryDto awbShippingHistoryDto = awbShippingHistoryService.addCommentToAwbShippingHistory(comment,awbId);
        return ResponseEntity.ok(awbShippingHistoryDto);
    }

    @GetMapping("/awb-shipping-history/awb/{id}")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity<AwbShippingHistoryDto> getAwbShippingHistoryByAwb(@PathVariable() Long id) {
        AwbShippingHistoryDto awbShippingHistoryDto = awbShippingHistoryService.findLatestAwbShippingHistoryByAwb(id);
        return ResponseEntity.ok(awbShippingHistoryDto);
    }

    @GetMapping("/awb-shipping-history/multiple-awb")
    @PreAuthorize("hasAuthority('READ_AWB_SHIPPING_HISTORY')")
    public ResponseEntity <Map<Long, List<AwbShippingHistoryDto>>> getAwbShippingHistoryByMultipleAwb(@RequestBody List<Long> awbIds) {
        Map<Long, List<AwbShippingHistoryDto>> awbShippingHistoryDto = awbShippingHistoryService.findShippingByAwbIds(awbIds);
        return ResponseEntity.ok(awbShippingHistoryDto);
    }
}
