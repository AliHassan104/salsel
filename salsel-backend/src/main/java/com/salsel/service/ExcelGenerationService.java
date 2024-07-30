package com.salsel.service;

import com.salsel.constants.ExcelConstants;
import com.salsel.dto.*;
import org.springframework.web.multipart.MultipartFile;

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
    Map<Long, List<ByteArrayOutputStream>> generateBillingStatements(List<Map<String, Object>> statementsMap);
    ByteArrayOutputStream generateEmployeeReport(List<Map<String, Object>> transitStatusReportData) throws IOException;
    ByteArrayOutputStream generateShipmentTrackingReport(List<Map<String, Object>> transitStatusReportData) throws IOException;
    void createExcelFile(List<Map<String, Object>> excelData, OutputStream outputStream, String type) throws IOException;
    void createExcelFileForCustomerStatement(List<Map<String, Object>> excelData, OutputStream outputStream, String type) throws IOException;
    void savePricingExcelData(MultipartFile file);
}
