package com.salsel.service;


import com.salsel.dto.BillingDto;
import com.salsel.model.BillingAttachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BillingService {
    List<BillingAttachment> save(MultipartFile file);
    List<BillingDto> uploadDataExcel(MultipartFile file);
    List<BillingDto> getAll(Boolean status);

    List<BillingDto> getAllBillingsWhereStatusIsNotClosed();

    BillingDto findById(Long id);

    List<Map<String,Object>> getBillingInvoiceDataByExcel();

    List<Map<String, Object>> getBillingInvoiceDataByExcelUploaded(List<BillingDto> billingDtoList);
//    Map<Long, List<BillingDto>> getAllGroupedByInvoice(Boolean status);

    void deleteById(Long id);
    void setToActiveById(Long id);


}
