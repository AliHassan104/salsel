package com.salsel.service;

import com.salsel.dto.CityDto;

import java.util.List;

public interface CityService {
    CityDto save(CityDto cityDto);
    List<CityDto> getAll(Boolean status);
    List<CityDto> getAllByCountry(Long countryId);
    CityDto findById(Long id);
    CityDto findByName(String name);
    void deleteById(Long id);
    void setToActiveById(Long id);
    CityDto update(Long id, CityDto cityDto);
}
