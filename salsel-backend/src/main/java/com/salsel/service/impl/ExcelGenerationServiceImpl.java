package com.salsel.service.impl;

import com.amazonaws.util.IOUtils;
import com.salsel.dto.AccountDto;
import com.salsel.dto.AwbDto;
import com.salsel.dto.TicketDto;
import com.salsel.dto.UserDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Role;
import com.salsel.service.AwbService;
import com.salsel.service.ExcelGenerationService;
import com.salsel.utils.HelperUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.salsel.constants.AwbStatusConstants.DELIVERED;
import static com.salsel.constants.AwbStatusConstants.PICKED_UP;
import static com.salsel.constants.BillingConstants.ACCOUNT_NUMBER;
import static com.salsel.constants.BillingConstants.INVOICES;
import static com.salsel.constants.ExcelConstants.*;

@Service
public class ExcelGenerationServiceImpl implements ExcelGenerationService {

    private final AwbService awbService;
    private final HelperUtils helperUtils;

    public ExcelGenerationServiceImpl(AwbService awbService, HelperUtils helperUtils) {
        this.awbService = awbService;
        this.helperUtils = helperUtils;
    }


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
            ticketData.put("PickupDate", ticket.getPickupDate() != null ? ticket.getPickupDate().format(formatter) : null);
            ticketData.put("PickupTime", ticket.getPickupTime() != null ? ticket.getPickupTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : null);
            ticketData.put("Ticket Category", ticket.getTicketCategory());
            ticketData.put("Ticket Sub Category", ticket.getTicketSubCategory());
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
    public List<Map<String, Object>> convertAirBillsToExcelData(List<AwbDto> airbills) {
        List<Map<String, Object>> excelData = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (AwbDto awb : airbills) {
            Map<String, Object> awbData = new LinkedHashMap<>();
            awbData.put("Id", awb.getId());
            awbData.put("CreatedAt", awb.getCreatedAt().format(formatter));
            awbData.put("Awb Number", awb.getUniqueNumber());
            awbData.put("CreatedBy", awb.getCreatedBy());
            awbData.put("ShipperName", awb.getShipperName());
            awbData.put("ShipperContactNumber", awb.getShipperContactNumber());
            awbData.put("OriginCountry", awb.getOriginCountry());
            awbData.put("OriginCity", awb.getOriginCity());
            awbData.put("PickupAddress", awb.getPickupAddress());
            awbData.put("PickupStreetName", awb.getPickupStreetName());
            awbData.put("PickupDistrict", awb.getPickupDistrict());
            awbData.put("ShipperRefNumber", awb.getShipperRefNumber());
            awbData.put("RecipientsName", awb.getRecipientsName());
            awbData.put("RecipientsContactNumber", awb.getRecipientsContactNumber());
            awbData.put("DestinationCountry", awb.getDestinationCountry());
            awbData.put("DestinationCity", awb.getDestinationCity());
            awbData.put("DeliveryAddress", awb.getDeliveryAddress());
            awbData.put("DeliveryStreetName", awb.getDeliveryStreetName());
            awbData.put("DeliveryDistrict", awb.getDeliveryDistrict());
            awbData.put("AccountNumber", awb.getAccountNumber());
            awbData.put("AssignedTo", awb.getAssignedTo());
            awbData.put("PickupDate", awb.getPickupDate() != null ? awb.getPickupDate().format(formatter) : null);
            awbData.put("PickupTime", awb.getPickupTime() != null ? awb.getPickupTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : null);
            awbData.put("ProductType", awb.getProductType());
            awbData.put("ServiceType", awb.getServiceType());
            awbData.put("RequestType", awb.getRequestType());
            awbData.put("Pieces", awb.getPieces());
            awbData.put("Content", awb.getContent());
            awbData.put("Weight", awb.getWeight());
            awbData.put("Amount", awb.getAmount());
            awbData.put("Currency", awb.getCurrency());
            awbData.put("DutyAndTaxesBillTo", awb.getDutyAndTaxesBillTo());
            awbData.put("Status", awb.getStatus().toString());
            awbData.put("AwbStatus", awb.getAwbStatus());

            excelData.add(awbData);
        }
        return excelData;
    }

    @Override
    public ByteArrayOutputStream generateAwbStatusReport(String status) throws IOException {
        List<Map<String, Object>> pickedUpReportData = awbService.getAwbByStatusChangedOnPreviousDay(status);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        createExcelFile(pickedUpReportData, outputStream, status);
        return outputStream;
    }

    @Override
    public ByteArrayOutputStream generateAwbTransitStatusReport() throws IOException {
        List<Map<String, Object>> transitStatusReportData = awbService.getAwbByStatusChangedLastDayExcludingPickedUpAndDelivered();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        createExcelFile(transitStatusReportData, outputStream, TRANSIT);
        return outputStream;
    }

    @Override
    public Map<Long, List<ByteArrayOutputStream>> generateBillingReports(List<Map<String, Object>> billingDataMaps) {
        Map<Long, List<ByteArrayOutputStream>> accountInvoicesMap = new LinkedHashMap<>();

        if(billingDataMaps == null){
            throw new RecordNotFoundException("Records not found for invoices.");
        }

        for (Map<String, Object> billingDataMap : billingDataMaps) {
            Long accountNumber = (Long) billingDataMap.get(ACCOUNT_NUMBER);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Generate the Excel file for the current account number
            try {
                createExcelFile((List<Map<String, Object>>) billingDataMap.get(INVOICES), outputStream, BILLING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Add the generated Excel file to the map
            if (accountInvoicesMap.containsKey(accountNumber)) {
                accountInvoicesMap.get(accountNumber).add(outputStream);
            } else {
                List<ByteArrayOutputStream> invoiceList = new ArrayList<>();
                invoiceList.add(outputStream);
                accountInvoicesMap.put(accountNumber, invoiceList);
            }
        }

        return accountInvoicesMap;
    }

    @Override
    public ByteArrayOutputStream generateEmployeeReport(List<Map<String, Object>> transitStatusReportData) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        createExcelFile(transitStatusReportData, outputStream, EMPLOYEE);
        return outputStream;
    }

    @Override
    public ByteArrayOutputStream generateShipmentTrackingReport(List<Map<String, Object>> transitStatusReportData) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        createExcelFile(transitStatusReportData, outputStream, TRACKING);
        return outputStream;
    }


    @Override
    public void createExcelFile(List<Map<String, Object>> excelData, OutputStream outputStream, String type) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        if (type.equalsIgnoreCase(USER_TYPE)) {
            sheet = workbook.createSheet("User");
        } else if (type.equalsIgnoreCase(TICKET_TYPE)) {
            sheet = workbook.createSheet("Ticket");
        } else if (type.equalsIgnoreCase(ACCOUNT_TYPE)) {
            sheet = workbook.createSheet("Account");
        } else if (type.equalsIgnoreCase(AWB_TYPE)) {
            sheet = workbook.createSheet("Awb");
        } else if (type.equalsIgnoreCase(PICKED_UP)) {
            sheet = workbook.createSheet("Picked Up Status Report");
        } else if (type.equalsIgnoreCase(DELIVERED)) {
            sheet = workbook.createSheet("Delivered Status Report");
        } else if (type.equalsIgnoreCase(TRANSIT)) {
            sheet = workbook.createSheet("Transit Report");
        } else if (type.equalsIgnoreCase(BILLING)) {
            sheet = workbook.createSheet("Billing Report");
        } else if (type.equalsIgnoreCase(SCANS)) {
            sheet = workbook.createSheet("Scan Report");
        }else if(type.equalsIgnoreCase(EMPLOYEE)){
            sheet = workbook.createSheet("Employee Report");
        }else if(type.equalsIgnoreCase(TRACKING)){
            sheet = workbook.createSheet("Tracking Report");
        }else {
            throw new RecordNotFoundException("Type not valid");
        }

        if (excelData == null || excelData.isEmpty()) {
            // If excelData is empty, write a message in the Excel file
            Row emptyRow = sheet.createRow(0);
            Cell emptyCell = emptyRow.createCell(0);
            emptyCell.setCellValue("No data available");
            workbook.write(outputStream);
            workbook.close();
            return;
        }

        int dataStartRow = 0;

        if (type.equalsIgnoreCase(BILLING)) {
            // Add the logo to the first row and first cell
            InputStream logoInputStream = helperUtils.getInputStreamFromUrl("https://api.salassilexpress.com/api/file/Logo/logo.jpeg");
            byte[] logoBytes = IOUtils.toByteArray(logoInputStream);
            int pictureIdx = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_JPEG);
            logoInputStream.close();

            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(0); // Column A
            anchor.setRow1(0); // Row 1
            anchor.setCol2(2); // Column C (for larger width)
            anchor.setRow2(4); // Row 4 (for larger height)

            drawing.createPicture(anchor, pictureIdx);

            // Shift the rows down to make space for the logo
            sheet.shiftRows(0, sheet.getLastRowNum(), 5);
            dataStartRow = 5; // Data should start from row 5
        }

        // Create header row
        Row headerRow = sheet.createRow(dataStartRow);
        int colIndex = 0;

        // Set bold font style for header
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

        for (String key : excelData.get(0).keySet()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(key);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows
        int rowIndex = dataStartRow + 1;
        for (Map<String, Object> rowData : excelData) {
            Row row = sheet.createRow(rowIndex++);
            colIndex = 0;
            for (Object value : rowData.values()) {
                Cell cell = row.createCell(colIndex++);
                if (value != null) {
                    if (value instanceof String) {
                        cell.setCellValue((String) value);
                    } else if (value instanceof Long) {
                        cell.setCellValue((Long) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Boolean) {
                        cell.setCellValue((Boolean) value);
                    } else if (value instanceof Date) {
                        cell.setCellValue((Date) value);
                    } else if (value instanceof Calendar) {
                        cell.setCellValue((Calendar) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                } else {
                    cell.setCellValue("N/A");
                }
            }
        }

        // Apply center alignment for all rows except header row
        for (int r = dataStartRow + 1; r < rowIndex; r++) {
            Row currentRow = sheet.getRow(r);
            for (int c = 0; c < excelData.get(0).size(); c++) {
                Cell currentCell = currentRow.getCell(c);
                if (currentCell != null) {
                    currentCell.setCellStyle(centerStyle);
                }
            }
        }

        // Auto-size columns
        for (int i = 0; i < excelData.get(0).size(); i++) {
            sheet.autoSizeColumn(i);
        }

       if(type.equalsIgnoreCase(BILLING)){
           // Add additional information at the right corner
           Row infoRow = sheet.createRow(1); // Skipping one row from the top
           Font boldFont = workbook.createFont();
           boldFont.setBold(true);
           Cell firstLineCell = infoRow.createCell(excelData.get(0).size() - 2); // Adjust the column position for the right corner
           firstLineCell.setCellValue("Salassil Express Shipping LLC");
           CellStyle boldStyle = workbook.createCellStyle();
           boldStyle.setFont(boldFont);
           firstLineCell.setCellStyle(boldStyle);

           Row secondLineRow = sheet.createRow(2); // Move to the next row
           Cell secondLineCell = secondLineRow.createCell(excelData.get(0).size() - 2); // Adjust the column position for the right corner
           secondLineCell.setCellValue("Dubai, UAE");
           secondLineCell.setCellStyle(boldStyle);
       }

        workbook.write(outputStream);
        workbook.close();
    }

}
