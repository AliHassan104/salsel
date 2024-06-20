package com.salsel.service;

import com.salsel.dto.StatementDto;

import java.util.List;
import java.util.Map;

public interface StatementService {
    List<Map<String, Object>> addStatements();
    StatementDto getStatementById();
}
