package com.salsel.controller;

import com.salsel.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatementController {
    private final StatementService statementService;

    @GetMapping("/statement")
    @PreAuthorize("hasAuthority('CREATE_STATEMENT') and hasAuthority('READ_STATEMENT')")
    public ResponseEntity<List<Map<String, Object>>> createStatement() {
        return ResponseEntity.ok(statementService.addStatements());
    }
}
