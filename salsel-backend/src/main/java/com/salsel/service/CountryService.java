package com.salsel.service;


import com.salsel.dto.CountryDto;
import com.salsel.dto.PaginationResponse;

import java.util.List;

public interface CountryService {
    CountryDto save(CountryDto countryDto);
    List<CountryDto> getAll();
    PaginationResponse getAllPaginatedCountry(Integer pageNumber, Integer pageSize);
    PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize);
    CountryDto findById(Long id);
    CountryDto findByName(String name);
    void deleteById(Long id);
    CountryDto update(Long id, CountryDto countryDto);
}
