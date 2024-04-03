package com.salsel.controller;

import com.salsel.dto.AwbStatusCommentDto;
import com.salsel.service.AwbStatusCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AwbStatusCommentController {
    private final AwbStatusCommentService awbStatusCommentService;

    public AwbStatusCommentController(AwbStatusCommentService awbStatusCommentService) {
        this.awbStatusCommentService = awbStatusCommentService;
    }

    @GetMapping("/awb-status-comment")
    @PreAuthorize("hasAuthority('AWB_STATUS_COMMENT')")
    public ResponseEntity<List<AwbStatusCommentDto>> getAllAwbStatusCommentByAwb(@RequestParam(value = "awbId") Long awbId) {
        List<AwbStatusCommentDto> awbStatusCommentDtoList = awbStatusCommentService.findByAwbId(awbId);
        return ResponseEntity.ok(awbStatusCommentDtoList);
    }

    @PostMapping("/awb-status-comment")
    @PreAuthorize("hasAuthority('AWB_STATUS_COMMENT')")
    public ResponseEntity<AwbStatusCommentDto> createAwbStatusComment(@RequestBody AwbStatusCommentDto awbStatusCommentDto) {
        AwbStatusCommentDto createdAwbStatusCommentDto = awbStatusCommentService.addComment(awbStatusCommentDto);
        return ResponseEntity.ok(createdAwbStatusCommentDto);
    }
}
