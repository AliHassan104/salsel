package com.salsel.service.impl;

import com.salsel.dto.CountryDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Country;
import com.salsel.repository.CountryRepository;
import com.salsel.service.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional
    public CountryDto save(CountryDto countryDto) {
        Country country = toEntity(countryDto);
        country.setStatus(true);
        Country createdCountry = countryRepository.save(country);
        return toDto(createdCountry);
    }

    @Override
    public List<CountryDto> getAll(Boolean status) {
        List<Country> countryList = countryRepository.findAllInDesOrderByIdAndStatus(status);
        List<CountryDto> countryDtoList = new ArrayList<>();

        for (Country country : countryList) {
            CountryDto countryDto = toDto(country);
            countryDtoList.add(countryDto);
        }
        return countryDtoList;
    }

    @Override
    public PaginationResponse getAllPaginatedCountry(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Country> pageCountry = countryRepository.findAllInDesOrderByIdAndStatus(page);
        List<Country> countryList = pageCountry.getContent();

        List<CountryDto> countryDtoList = new ArrayList<>();
        for (Country country : countryList) {
            CountryDto countryDto = toDto(country);
            countryDtoList.add(countryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(countryDtoList);
        paginationResponse.setPageNumber(pageCountry.getNumber());
        paginationResponse.setPageSize(pageCountry.getSize());
        paginationResponse.setTotalElements(pageCountry.getNumberOfElements());
        paginationResponse.setTotalPages(pageCountry.getTotalPages());
        paginationResponse.setLastPage(pageCountry.isLast());

        return paginationResponse;
    }

    @Override
    public PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<Country> pageCountry = countryRepository.findCountryByName(name,page);
        List<Country> countryList = pageCountry.getContent();

        List<CountryDto> countryDtoList = new ArrayList<>();
        for (Country country : countryList) {
            CountryDto countryDto = toDto(country);
            countryDtoList.add(countryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(countryDtoList);
        paginationResponse.setPageNumber(pageCountry.getNumber());
        paginationResponse.setPageSize(pageCountry.getSize());
        paginationResponse.setTotalElements(pageCountry.getNumberOfElements());
        paginationResponse.setTotalPages(pageCountry.getTotalPages());
        paginationResponse.setLastPage(pageCountry.isLast());

        return paginationResponse;
    }

    @Override
    public CountryDto findById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Country not found for id => %d", id)));
        return toDto(country);
    }

    @Override
    public CountryDto findByName(String name) {
        Country country = countryRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Country not found for name => %s", name)));
        return toDto(country);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Country not found for id => %d", id)));
        countryRepository.setStatusInactive(country.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Country not found for id => %d", id)));
        countryRepository.setStatusActive(country.getId());
    }

    @Override
    @Transactional
    public CountryDto update(Long id, CountryDto countryDto) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Country not found for id => %d", id)));

        existingCountry.setName(countryDto.getName());

        Country updatedCountry = countryRepository.save(existingCountry);
        return toDto(updatedCountry);
    }

    public CountryDto toDto(Country country) {
        return CountryDto.builder()
                .id(country.getId())
                .code(country.getCode())
                .alphaCode(country.getAlphaCode())
                .name(country.getName())
                .status(country.getStatus())
                .build();
    }

    public Country toEntity(CountryDto countryDto) {
        return Country.builder()
                .id(countryDto.getId())
                .code(countryDto.getCode())
                .alphaCode(countryDto.getAlphaCode())
                .name(countryDto.getName())
                .status(countryDto.getStatus())
                .build();
    }

}
