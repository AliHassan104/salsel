package com.salsel.service;

import com.salsel.constants.ExcelConstants;
import com.salsel.dto.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface ExcelGenerationService {
    List<Map<String, Object>> convertUsersToExcelData(List<UserDto> users);
    List<Map<String, Object>> convertTicketsToExcelData(List<TicketDto> tickets);
    List<Map<String, Object>> convertAccountsToExcelData(List<AccountDto> accounts);
    List<Map<String,Object>> convertAirBillsToExcelData(List<AwbDto> airbills);
    ByteArrayOutputStream generateAwbStatusReport(String status) throws IOException;
    ByteArrayOutputStream generateAwbTransitStatusReport() throws IOException;

    Map<Long, List<ByteArrayOutputStream>> generateBillingReports(List<Map<String, Object>> billingMaps);


    void createExcelFile(List<Map<String, Object>> excelData, OutputStream outputStream, String type) throws IOException;
}
