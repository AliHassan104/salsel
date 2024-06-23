package com.salsel.service.impl;

import com.salsel.dto.AwbShippingHistoryDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.model.AwbShippingHistory;
import com.salsel.repository.AwbRepository;
import com.salsel.repository.AwbShippingHistoryRepository;
import com.salsel.service.AwbShippingHistoryService;
import com.salsel.utils.HelperUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AwbShippingHistoryServiceImpl implements AwbShippingHistoryService {

    private final AwbShippingHistoryRepository awbShippingHistoryRepository;
    private final HelperUtils helperUtils;

    private final AwbRepository awbRepository;

    public AwbShippingHistoryServiceImpl(AwbShippingHistoryRepository awbShippingHistoryRepository, HelperUtils helperUtils, AwbRepository awbRepository) {
        this.awbShippingHistoryRepository = awbShippingHistoryRepository;
        this.helperUtils = helperUtils;
        this.awbRepository = awbRepository;
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
                return toDto(latestHistory);
            }
        }

        AwbShippingHistory awbShippingHistory = new AwbShippingHistory();
        awbShippingHistory.setStatus(true);
        awbShippingHistory.setAwbStatus(awb.getAwbStatus());
        awbShippingHistory.setPdaScan(awb.getPdaScan());
        awbShippingHistory.setAwb(awb);
        awbShippingHistory.setStatusUpdateByUser(helperUtils.getCurrentUser());

        AwbShippingHistory createdHistory = awbShippingHistoryRepository.save(awbShippingHistory);
        return toDto(createdHistory);
    }

    @Transactional
    public AwbShippingHistoryDto addAwbShippingHistoryForMobileApp(Awb awb) {
        // Retrieve the latest shipping history entry for the AWB
        Optional<AwbShippingHistory> latestHistoryOptional = awbShippingHistoryRepository.findTop1ByAwbIdOrderByTimestampDesc(awb.getId());

        // Check if the latest history entry exists and has the same status as the current AWB
        if (latestHistoryOptional.isPresent()) {
            AwbShippingHistory latestHistory = latestHistoryOptional.get();
            if (latestHistory.getPdaScan().equals(awb.getPdaScan())) {
                return toDto(latestHistory);
            }
        }

        AwbShippingHistory awbShippingHistory = new AwbShippingHistory();
        awbShippingHistory.setStatus(true);
        awbShippingHistory.setAwbStatus(awb.getAwbStatus());
        awbShippingHistory.setPdaScan(awb.getPdaScan());
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

    @Override
    public Map<Long, List<AwbShippingHistoryDto>> findShippingByAwbIds(List<Long> awbIds) {
        Map<Long, List<AwbShippingHistoryDto>> shippingHistoryMap = new HashMap<>();

        for (Long awbId : awbIds) {
            // Find shipping history for each AWB ID
            List<AwbShippingHistory> awbShippingHistoryList = awbShippingHistoryRepository.findByAwbId(awbId);

            if (awbShippingHistoryList.isEmpty()) {
                throw new RecordNotFoundException("AwbShippingHistory not found for id => " + awbId);
            }

            // Convert each entity to DTO
            List<AwbShippingHistoryDto> shippingHistoryDtoList = awbShippingHistoryList.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            // Put the list of DTOs in the map with the AWB ID as the key
            shippingHistoryMap.put(awbId, shippingHistoryDtoList);
        }

        return shippingHistoryMap;
    }

    @Override
    public List<AwbShippingHistoryDto> findTrackingByAwbIds(List<Long> awbIds) {
        List<AwbShippingHistoryDto> awbShippingHistoryDtoList = new ArrayList<>();
        Set<Long> processedTrackingNumbers = new HashSet<>();

        if (awbIds == null) {
            throw new IllegalArgumentException("AWB IDs list cannot be null");
        }

        for (Long awbId : awbIds) {

            Awb awb = awbRepository.findByTrackingNumber(awbId);
          if(awb != null){
    if (processedTrackingNumbers.contains(awbId)) {
        continue;
    }

    List<AwbShippingHistory> awbShippingHistoryList = awbShippingHistoryRepository.findByAwbId(awb.getId());


    if (awbShippingHistoryList != null && !awbShippingHistoryList.isEmpty()) {
        // Find the AwbShippingHistory with the latest timestamp
        AwbShippingHistory latestHistory = awbShippingHistoryList.stream()
                .filter(Objects::nonNull)
                .max(Comparator.comparing(AwbShippingHistory::getTimestamp))
                .orElse(null);


        if (latestHistory != null) {
            AwbShippingHistoryDto awbShippingHistoryDto = toDto(latestHistory);
            awbShippingHistoryDtoList.add(awbShippingHistoryDto);
            processedTrackingNumbers.add(awbId);
        }
    }
}
        }

        return awbShippingHistoryDtoList;
    }

    @Override
    public List<Map<String, Object>> getAllShippingDataByExcel(List<Long> awbIds) {
        List<AwbShippingHistoryDto> awbShippingHistoryDtoList = findTrackingByAwbIds(awbIds);

        List<Map<String, Object>> result = new ArrayList<>();
        int count = 1;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        for (AwbShippingHistoryDto awbShippingHistoryDto : awbShippingHistoryDtoList) {
            Map<String, Object> shippingMap = new LinkedHashMap<>();
            shippingMap.put("#", count++);
            shippingMap.put("Tracking Number", awbShippingHistoryDto.getAwb().getUniqueNumber());
            shippingMap.put("Scan Date", awbShippingHistoryDto.getTimestamp().format(dateFormatter));
            shippingMap.put("Scan Time", awbShippingHistoryDto.getTimestamp().format(timeFormatter));
            shippingMap.put("Scanned By", awbShippingHistoryDto.getStatusUpdateByUser().getName());
            shippingMap.put("Current Status", awbShippingHistoryDto.getAwbStatus());
            shippingMap.put("Location", awbShippingHistoryDto.getStatusUpdateByUser().getCountry());

            result.add(shippingMap);
        }

        return result;
    }

    public AwbShippingHistoryDto toDto(AwbShippingHistory awbShippingHistory) {
        return AwbShippingHistoryDto.builder()
                .id(awbShippingHistory.getId())
                .timestamp(awbShippingHistory.getTimestamp())
                .status(awbShippingHistory.getStatus())
                .awbStatus(awbShippingHistory.getAwbStatus())
                .comment(awbShippingHistory.getComment())
                .statusUpdateByUser(awbShippingHistory.getStatusUpdateByUser())
                .pdaScan(awbShippingHistory.getPdaScan())
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
                .pdaScan(awbShippingHistoryDto.getPdaScan())
                .awb(awbShippingHistoryDto.getAwb())
                .build();
    }
}
