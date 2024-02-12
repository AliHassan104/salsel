package com.salsel.service;

import com.salsel.dto.AccountDto;
import com.salsel.dto.AwbDto;

import java.util.List;

public interface AwbService {
    AwbDto save(AwbDto awbDto);
    byte[] downloadAwbPdf(String fileName, Long awbId);
    List<AwbDto> getAwbByLoggedInUser(Boolean status);
    List<AwbDto> getAwbByLoggedInUserRole(Boolean status);
    List<AwbDto> getAwbByLoggedInUserAndRole(Boolean status);
    List<AwbDto> getAll(Boolean status);
    AwbDto findById(Long id);
    void deleteById(Long id);
    void setToActiveById(Long id);
    AwbDto update(Long id, AwbDto awbDto);
    AwbDto updateAwbStatusOnScan(Long uniqueNumber);
}
