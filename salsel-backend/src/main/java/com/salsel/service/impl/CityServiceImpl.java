package com.salsel.service.impl;

import com.salsel.dto.CityDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.City;
import com.salsel.model.Country;
import com.salsel.repository.CityRepository;
import com.salsel.repository.CountryRepository;
import com.salsel.service.CityService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public CityServiceImpl(CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Override
    @Transactional
    public CityDto save(CityDto cityDto) {
        City city = toEntity(cityDto);
        city.setStatus(true);

        Country country = countryRepository.findById(city.getCountry().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Country not found for id => %d", city.getCountry().getId())));

        city.setCountry(country);
        City createdCity = cityRepository.save(city);
        return toDto(createdCity);
    }

    @Override
    public List<CityDto> getAll() {
        List<City> cityList = cityRepository.findAllInDesOrderByIdAndStatus();
        List<CityDto> cityDtoList = new ArrayList<>();

        for (City city : cityList) {
            CityDto cityDto = toDto(city);
            cityDtoList.add(cityDto);
        }
        return cityDtoList;
    }

    @Override
    public CityDto findById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("City not found for id => %d", id)));
        return toDto(city);
    }

    @Override
    public CityDto findByName(String name) {
        City city = cityRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("City not found for name => %s", name)));
        return toDto(city);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("City not found for id => %d", id)));
        cityRepository.setStatusInactive(city.getId());
    }

    @Override
    @Transactional
    public CityDto update(Long id, CityDto cityDto) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("City not found for id => %d", id)));

        existingCity.setName(cityDto.getName());

        existingCity.setCountry(countryRepository.findById(cityDto.getCountry().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Country not found for id => %d", cityDto.getCountry().getId()))));

        City updatedCity = cityRepository.save(existingCity);
        return toDto(updatedCity);
    }

    public CityDto toDto(City city) {
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .status(city.getStatus())
                .country(city.getCountry())
                .build();
    }

    public City toEntity(CityDto cityDto) {
        return City.builder()
                .id(cityDto.getId())
                .name(cityDto.getName())
                .status(cityDto.getStatus())
                .country(cityDto.getCountry())
                .build();
    }
}
