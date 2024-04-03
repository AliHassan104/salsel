package com.salsel.service.impl;

import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.model.AwbShippingHistory;
import com.salsel.repository.AwbShippingHistoryRepository;
import com.salsel.service.AwbShippingHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwbShippingHistoryServiceImpl implements AwbShippingHistoryService {

    private final AwbShippingHistoryRepository awbShippingHistoryRepository;

    public AwbShippingHistoryServiceImpl(AwbShippingHistoryRepository awbShippingHistoryRepository) {
        this.awbShippingHistoryRepository = awbShippingHistoryRepository;
    }

    @Override
    public List<AwbShippingHistoryDto> findByAwbId(Long awbId) {
        List<AwbShippingHistory> awbShippingHistoryList = awbShippingHistoryRepository.findByAwbId(awbId);
        return awbShippingHistoryList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AwbShippingHistoryDto toDto(AwbShippingHistory awbShippingHistory) {
        return AwbShippingHistoryDto.builder()
                .id(awbShippingHistory.getId())
                .timestamp(awbShippingHistory.getTimestamp())
                .status(awbShippingHistory.getStatus())
                .awbStatus(awbShippingHistory.getAwbStatus())
                .awb(awbShippingHistory.getAwb())
                .build();
    }

    public AwbShippingHistory toEntity(AwbShippingHistoryDto awbShippingHistoryDto) {
        return AwbShippingHistory.builder()
                .id(awbShippingHistoryDto.getId())
                .timestamp(awbShippingHistoryDto.getTimestamp())
                .status(awbShippingHistoryDto.getStatus())
                .awbStatus(awbShippingHistoryDto.getAwbStatus())
                .awb(awbShippingHistoryDto.getAwb())
                .build();
    }
}
