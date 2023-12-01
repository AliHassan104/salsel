package com.salsel.service;

import com.salsel.dto.CityDto;
import com.salsel.dto.PaginationResponse;

import java.util.List;

public interface CityService {
    CityDto save(CityDto cityDto);
    List<CityDto> getAll();
    CityDto findById(Long id);
    CityDto findByName(String name);
    void deleteById(Long id);
    CityDto update(Long id, CityDto cityDto);
}
