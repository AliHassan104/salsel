package com.salsel.service;


import com.salsel.dto.AccountDto;
import com.salsel.dto.BillingDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BillingService {
    BillingDto save(BillingDto billingDto);
    List<BillingDto> uploadDataExcel(MultipartFile file);
    List<BillingDto> getAll(Boolean status);

    List<BillingDto> getAllBillingsWhereStatusIsNotClosed();

    BillingDto findById(Long id);

    List<Map<String,Object>> getBillingInvoiceDataByExcel();

//    Map<Long, List<BillingDto>> getAllGroupedByInvoice(Boolean status);
}
