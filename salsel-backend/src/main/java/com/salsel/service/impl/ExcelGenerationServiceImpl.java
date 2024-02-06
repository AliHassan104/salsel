package com.salsel.service.impl;

import com.salsel.constants.ExcelConstants;
import com.salsel.dto.AccountDto;
import com.salsel.dto.TicketDto;
import com.salsel.dto.UserDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Role;
import com.salsel.service.ExcelGenerationService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.salsel.constants.ExcelConstants.*;

@Service
public class ExcelGenerationServiceImpl implements ExcelGenerationService {
    @Override
    public List<Map<String, Object>> convertUsersToExcelData(List<UserDto> users) {
        List<Map<String, Object>> excelData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Define your desired date format

        for (UserDto user : users) {
            Map<String, Object> userData = new LinkedHashMap<>();
            userData.put("Id", user.getId());
            userData.put("CreatedAt", user.getCreatedAt().format(formatter));
            userData.put("EmployeeId", user.getEmployeeId());
            userData.put("Name", user.getName());
            userData.put("FirstName", user.getFirstname());
            userData.put("LastName", user.getLastname());
            userData.put("Phone", user.getPhone());
            userData.put("Email", user.getEmail());
            userData.put("Password", user.getPassword());
            userData.put("Status", user.getStatus().toString());

            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet());
            userData.put("Roles", String.join(", ", roleNames));

            excelData.add(userData);
        }
        return excelData;
    }

    @Override
    public List<Map<String, Object>> convertTicketsToExcelData(List<TicketDto> tickets) {
        List<Map<String, Object>> excelData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (TicketDto ticket : tickets) {
            Map<String, Object> ticketData = new LinkedHashMap<>();
            ticketData.put("Id", ticket.getId());
            ticketData.put("CreatedAt", ticket.getCreatedAt().format(formatter));
            ticketData.put("ShipperName", ticket.getShipperName());
            ticketData.put("ShipperContactNumber", ticket.getShipperContactNumber());
            ticketData.put("PickupAddress", ticket.getPickupAddress());
            ticketData.put("ShipperRefNumber", ticket.getShipperRefNumber());
            ticketData.put("RecipientName", ticket.getRecipientName());
            ticketData.put("RecipientContactNumber", ticket.getRecipientContactNumber());
            ticketData.put("DeliveryAddress", ticket.getDeliveryAddress());
            ticketData.put("DeliveryStreetName", ticket.getDeliveryStreetName());
            ticketData.put("DeliveryDistrict", ticket.getDeliveryDistrict());
            ticketData.put("PickupStreetName", ticket.getPickupStreetName());
            ticketData.put("PickupDistrict", ticket.getPickupDistrict());
            ticketData.put("Name", ticket.getName());
            ticketData.put("Weight", ticket.getWeight());
            ticketData.put("Email", ticket.getEmail());
            ticketData.put("Phone", ticket.getPhone());
            ticketData.put("Textarea", ticket.getTextarea());
            ticketData.put("AirwayNumber", ticket.getAirwayNumber());
            ticketData.put("TicketType", ticket.getTicketType());
            ticketData.put("TicketUrl", ticket.getTicketUrl());
            ticketData.put("PickupDate", ticket.getPickupDate().format(formatter));
            ticketData.put("PickupTime", ticket.getPickupTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            ticketData.put("Category", ticket.getCategory());
            ticketData.put("TicketFlag", ticket.getTicketFlag());
            ticketData.put("AssignedTo", ticket.getAssignedTo());
            ticketData.put("OriginCountry", ticket.getOriginCountry());
            ticketData.put("OriginCity", ticket.getOriginCity());
            ticketData.put("DestinationCountry", ticket.getDestinationCountry());
            ticketData.put("DestinationCity", ticket.getDestinationCity());
            ticketData.put("CreatedBy", ticket.getCreatedBy());
            ticketData.put("Department", ticket.getDepartment());
            ticketData.put("DepartmentCategory", ticket.getDepartmentCategory());
            ticketData.put("TicketStatus", ticket.getTicketStatus());
            ticketData.put("Status", ticket.getStatus().toString());

            excelData.add(ticketData);
        }
        return excelData;
    }

    @Override
    public List<Map<String, Object>> convertAccountsToExcelData(List<AccountDto> accounts) {
        List<Map<String, Object>> excelData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (AccountDto account : accounts) {
            Map<String, Object> accountData = new LinkedHashMap<>();
            accountData.put("Id", account.getId());
            accountData.put("CreatedAt", account.getCreatedAt().format(formatter));
            accountData.put("AccountType", account.getAccountType());
            accountData.put("Email", account.getEmail());
            accountData.put("BusinessActivity", account.getBusinessActivity());
            accountData.put("AccountNumber", account.getAccountNumber());
            accountData.put("CustomerName", account.getCustomerName());
            accountData.put("ProjectName", account.getProjectName());
            accountData.put("TradeLicenseNo", account.getTradeLicenseNo());
            accountData.put("TaxDocumentNo", account.getTaxDocumentNo());
            accountData.put("County", account.getCounty());
            accountData.put("City", account.getCity());
            accountData.put("Address", account.getAddress());
            accountData.put("CustName", account.getCustName());
            accountData.put("ContactNumber", account.getContactNumber());
            accountData.put("BillingPocName", account.getBillingPocName());
            accountData.put("SalesAgentName", account.getSalesAgentName());
            accountData.put("SalesRegion", account.getSalesRegion());
            accountData.put("AccountUrl", account.getAccountUrl());
            accountData.put("Status", account.getStatus().toString());

            excelData.add(accountData);
        }
        return excelData;
    }

    @Override
    public void createExcelFile(List<Map<String, Object>> excelData, OutputStream outputStream, String type) throws IOException {
        if (excelData == null || excelData.isEmpty()) {
            throw new IllegalArgumentException("Excel data is empty");
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        if(type.equalsIgnoreCase(USER_TYPE)){
            sheet = workbook.createSheet("User");
        } else if (type.equalsIgnoreCase(TICKET_TYPE)) {
            sheet = workbook.createSheet("Ticket");
        } else if (type.equalsIgnoreCase(ACCOUNT_TYPE)){
            sheet = workbook.createSheet("Account");
        } else{
            throw new RecordNotFoundException("Type not valid");
        }

        // Create header row
        Row headerRow = sheet.createRow(0);
        int colIndex = 0;
        for (String key : excelData.get(0).keySet()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(key);
        }

        // Populate data rows
        int rowIndex = 1;
        for (Map<String, Object> rowData : excelData) {
            Row row = sheet.createRow(rowIndex++);
            colIndex = 0;
            for (Object value : rowData.values()) {
                Cell cell = row.createCell(colIndex++);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                }
                // Add more data type handling as needed
            }
        }

        // Auto-size columns
        for (int i = 0; i < excelData.get(0).size(); i++) {
            sheet.autoSizeColumn(i);
        }
        workbook.write(outputStream);
        workbook.close();
    }

}
