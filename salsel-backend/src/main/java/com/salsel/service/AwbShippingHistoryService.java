package com.salsel.service;

import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.model.Awb;
import com.salsel.model.AwbShippingHistory;

import java.util.List;
import java.util.Map;

public interface AwbShippingHistoryService {
    AwbShippingHistoryDto addAwbShippingHistory(Awb awb);
    AwbShippingHistoryDto addCommentToAwbShippingHistory(String comment, Long awbId);
    AwbShippingHistoryDto findLatestAwbShippingHistoryByAwb(Long awbId);
    List<AwbShippingHistoryDto> findByAwbId(Long awbId);

    Map<Long, List<AwbShippingHistoryDto>> findShippingByAwbIds(List<Long> awbIds);

    List<AwbShippingHistoryDto> findTrackingByAwbIds(List<Long> awbIds);

}
