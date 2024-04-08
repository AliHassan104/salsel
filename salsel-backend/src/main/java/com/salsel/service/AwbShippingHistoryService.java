package com.salsel.service;

import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.model.Awb;
import com.salsel.model.AwbShippingHistory;

import java.util.List;

public interface AwbShippingHistoryService {
    AwbShippingHistoryDto addAwbShippingHistory(Awb awb);
    AwbShippingHistoryDto addCommentToAwbShippingHistory(String comment, Long awbId);
    AwbShippingHistoryDto findLatestAwbShippingHistoryByAwb(Long awbId);
    List<AwbShippingHistoryDto> findByAwbId(Long awbId);
}
