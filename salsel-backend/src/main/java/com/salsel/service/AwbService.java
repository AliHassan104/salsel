package com.salsel.service;

import com.salsel.dto.AwbDto;

import java.util.List;

public interface AwbService {
    AwbDto save(AwbDto awbDto);
    byte[] downloadAwbPdf(String fileName, Long awbId);
    List<AwbDto> getAll(Boolean status);
    AwbDto findById(Long id);
    void deleteById(Long id);
    void setToActiveById(Long id);
    AwbDto update(Long id, AwbDto awbDto);
}
