package com.salsel.service;

import com.salsel.dto.AwbDto;

import java.util.List;

public interface AwbService {
    AwbDto save(AwbDto awbDto);
    List<AwbDto> getAll();
    AwbDto findById(Long id);
    void deleteById(Long id);
    AwbDto update(Long id, AwbDto awbDto);
}
