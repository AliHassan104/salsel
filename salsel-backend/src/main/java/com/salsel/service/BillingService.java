package com.salsel.service;


import com.salsel.dto.BillingDto;

import java.util.List;
import java.util.Map;

public interface BillingService {
    BillingDto save(BillingDto billingDto);
    List<BillingDto> getAll(Boolean status);

    List<BillingDto> getAllBillingsWhereStatusIsNotClosed();

    BillingDto findById(Long id);

    List<Map<String,Object>> getBillingByExcel();
}
