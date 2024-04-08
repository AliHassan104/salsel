package com.salsel.service.impl;

import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.model.AwbShippingHistory;
import com.salsel.repository.AwbShippingHistoryRepository;
import com.salsel.service.AwbShippingHistoryService;
import com.salsel.utils.HelperUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AwbShippingHistoryServiceImpl implements AwbShippingHistoryService {

    private final AwbShippingHistoryRepository awbShippingHistoryRepository;
    private final HelperUtils helperUtils;

    public AwbShippingHistoryServiceImpl(AwbShippingHistoryRepository awbShippingHistoryRepository, HelperUtils helperUtils) {
        this.awbShippingHistoryRepository = awbShippingHistoryRepository;
        this.helperUtils = helperUtils;
    }

    @Override
    @Transactional
    public AwbShippingHistoryDto addAwbShippingHistory(Awb awb) {
        // Retrieve the latest shipping history entry for the AWB
        Optional<AwbShippingHistory> latestHistoryOptional = awbShippingHistoryRepository.findTop1ByAwbIdOrderByTimestampDesc(awb.getId());

        // Check if the latest history entry exists and has the same status as the current AWB
        if (latestHistoryOptional.isPresent()) {
            AwbShippingHistory latestHistory = latestHistoryOptional.get();
            if (latestHistory.getAwbStatus().equals(awb.getAwbStatus())) {
                // The latest shipping history entry already has the same status, so return its DTO
                return toDto(latestHistory);
            }
        }

        AwbShippingHistory awbShippingHistory = new AwbShippingHistory();
        awbShippingHistory.setStatus(true);
        awbShippingHistory.setAwbStatus(awb.getAwbStatus());
        awbShippingHistory.setAwb(awb);
        awbShippingHistory.setStatusUpdateByUser(helperUtils.getCurrentUser());

        AwbShippingHistory createdHistory = awbShippingHistoryRepository.save(awbShippingHistory);
        return toDto(createdHistory);
    }

    @Override
    @Transactional
    public AwbShippingHistoryDto addCommentToAwbShippingHistory(String comment, Long awbId) {
        AwbShippingHistory latestAwbHistory = awbShippingHistoryRepository.findTop1ByAwbIdOrderByTimestampDesc(awbId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Awb not found for id => %d", awbId)));

        if (comment == null || comment.isEmpty()) {
            latestAwbHistory.setComment(null);
        } else {
            latestAwbHistory.setComment(comment);
        }
        return toDto(latestAwbHistory);
    }

    @Override
    public AwbShippingHistoryDto findLatestAwbShippingHistoryByAwb(Long awbId) {
        AwbShippingHistory awbShippingHistory = awbShippingHistoryRepository.findTop1ByAwbIdOrderByTimestampDesc(awbId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("AwbShippingHistory not found for id => %d", awbId)));
        return toDto(awbShippingHistory);
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
                .comment(awbShippingHistory.getComment())
                .statusUpdateByUser(awbShippingHistory.getStatusUpdateByUser())
                .awb(awbShippingHistory.getAwb())
                .build();
    }

    public AwbShippingHistory toEntity(AwbShippingHistoryDto awbShippingHistoryDto) {
        return AwbShippingHistory.builder()
                .id(awbShippingHistoryDto.getId())
                .timestamp(awbShippingHistoryDto.getTimestamp())
                .status(awbShippingHistoryDto.getStatus())
                .comment(awbShippingHistoryDto.getComment())
                .statusUpdateByUser(awbShippingHistoryDto.getStatusUpdateByUser())
                .awbStatus(awbShippingHistoryDto.getAwbStatus())
                .awb(awbShippingHistoryDto.getAwb())
                .build();
    }
}
