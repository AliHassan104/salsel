package com.salsel.service;

import com.salsel.dto.AccountDto;
import com.salsel.dto.AwbDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AwbService {
    AwbDto save(AwbDto awbDto);
    byte[] downloadAwbPdf(String fileName, Long awbId);
    List<AwbDto> getAwbByLoggedInUser(Boolean status);
    List<AwbDto> getAwbByAssignedUser(String user, Boolean status);
    List<AwbDto> getAwbByLoggedInUserRole(Boolean status);
    List<AwbDto> getAwbByLoggedInUserAndRole(Boolean status);
    List<AwbDto> getAll(Boolean status);
    AwbDto findById(Long id);
    void deleteById(Long id);
    void setToActiveById(Long id);
    AwbDto update(Long id, AwbDto awbDto);
    AwbDto updateAwbStatusOnScan(Long uniqueNumber, String awbStatus);
    List<AwbDto> updateMultipleAwbStatusOnScan(Map<Long,String> statusMap);
    List<AwbDto> getAwbBetweenDates(LocalDate startDate, LocalDate endDate);
    AwbDto findByUniqueNumber(Long id);
    Map<String, Long> getAwbStatusCounts();
    Map<String, Long> getStatusCounts();
    Map<String, Long> getAwbStatusCountsBasedOnLoggedInUser();
    Map<String, Long> getStatusCountsBasedOnLoggedInUser();
    LocalDate getMinCreatedAt();
    LocalDate getMaxCreatedAt();
    Long getAllAwbByAssignedUser();
}
