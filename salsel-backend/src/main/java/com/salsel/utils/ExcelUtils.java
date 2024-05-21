package com.salsel.utils;

import com.salsel.exception.BillingException;
import com.salsel.model.Billing;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ExcelUtils {

    public static boolean isExcelFile(MultipartFile file) {
        // Check file extension
        String fileName = file.getOriginalFilename();
        if (fileName != null && (fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
            return true;
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType != null && (MediaType.APPLICATION_OCTET_STREAM.toString().equals(contentType) ||
                MediaType.APPLICATION_XML.toString().equals(contentType) ||
                "application/vnd.ms-excel".equals(contentType) ||
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType))) {
            return true;
        }
        return false;
    }

    public static boolean validateExcelHeaders(MultipartFile multipartFile) {
        String[] expectedHeaders = {"Customer Account", "Date", "Transaction Number", "Customer Ref#", "Product", "Service Details", "Charges", "Tax Amount"};

        try (Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming the headers are in the first sheet
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                // If the first row is null, return false
                return false;
            }

            int lastCellNum = headerRow.getLastCellNum();
            if (lastCellNum != expectedHeaders.length) {
                // If the number of headers doesn't match, return false
                return false;
            }

            for (int i = 0; i < lastCellNum; i++) {
                Cell cell = headerRow.getCell(i);
                if(cell != null){
                    log.info(cell.getStringCellValue() + " == " + expectedHeaders[i]);
                    if (!cell.getStringCellValue().equals(expectedHeaders[i])) {
                        // If any header doesn't match, return false
                        return false;
                    }
                }
            }

            // All headers match, return true
            return true;
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Billing> processExcelFile(MultipartFile multipartFile) {
        String invoiceNo = null;
        List<Billing> billingList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        try (Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming data starts from the second row
            int rowsCount = sheet.getPhysicalNumberOfRows();

            for (int rowNum = 1; rowNum < rowsCount; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row != null) {
                    Billing billing = new Billing();
                    billing.setCustomerAccountNumber((long) (row.getCell(0).getNumericCellValue()));

                    Date invoiceDate = row.getCell(1).getDateCellValue();
                    LocalDate localDate = invoiceDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    billing.setInvoiceDate(localDate);

                    billing.setAirwayBillNo((long) row.getCell(2).getNumericCellValue());
                    String customerRef = dataFormatter.formatCellValue(row.getCell(3));
                    billing.setCustomerRef(customerRef);
                    billing.setProduct(row.getCell(4).getStringCellValue());
                    billing.setServiceDetails(row.getCell(5).getStringCellValue());
                    billing.setCharges(row.getCell(6).getNumericCellValue());
                    billing.setTaxAmount(row.getCell(7).getNumericCellValue());

                    billing.setStatus(true);
                    billingList.add(billing);
                }
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            throw new BillingException("Something went wrong while processing excel");
        }

        return billingList;
    }
}
