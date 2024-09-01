package com.salsel.service;

import com.salsel.dto.RatesDto;

import java.util.List;

public interface RatesService {
    RatesDto addRates(RatesDto ratesDto);
    Double getRate(String fromCountry, String toCountry, String product, Double productWeight);
    List<RatesDto> getAll(Boolean status);
    RatesDto findById(Long id);
    RatesDto updateRates(Long id, RatesDto ratesDto);
    void deleteRates(Long id);
    void setToActiveById(Long id);
}
