package com.salsel.service;

import com.salsel.dto.AwbShippingHistoryDto;

import java.util.List;

public interface AwbShippingHistoryService {
    List<AwbShippingHistoryDto> findByAwbId(Long awbId);
}
