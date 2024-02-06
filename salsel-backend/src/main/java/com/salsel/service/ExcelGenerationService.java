package com.salsel.service;

import com.salsel.constants.ExcelConstants;
import com.salsel.dto.AccountDto;
import com.salsel.dto.TicketDto;
import com.salsel.dto.UserDto;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ExcelGenerationService {
    List<Map<String, Object>> convertUsersToExcelData(List<UserDto> users);
    List<Map<String, Object>> convertTicketsToExcelData(List<TicketDto> tickets);
    List<Map<String, Object>> convertAccountsToExcelData(List<AccountDto> accounts);
    void createExcelFile(List<Map<String, Object>> excelData, OutputStream outputStream, String type) throws IOException;
}
