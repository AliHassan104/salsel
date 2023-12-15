package com.salsel.controller;

import com.salsel.dto.CityDto;
import com.salsel.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/city")
    @PreAuthorize("hasAuthority('CREATE_CITY') and hasAuthority('READ_CITY')")
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        return ResponseEntity.ok(cityService.save(cityDto));
    }

    @GetMapping("/city")
    @PreAuthorize("hasAuthority('READ_CITY')")
    public ResponseEntity<List<CityDto>> getAllCity() {
        List<CityDto> cityDtoList = cityService.getAll();
        return ResponseEntity.ok(cityDtoList);
    }

    @GetMapping("/city/country/{id}")
    @PreAuthorize("hasAuthority('READ_CITY')")
    public ResponseEntity<List<CityDto>> getAllCitiesByCountry(@PathVariable Long id) {
        List<CityDto> cityDtoList = cityService.getAllByCountry(id);
        return ResponseEntity.ok(cityDtoList);
    }

    @GetMapping("/city/{id}")
    @PreAuthorize("hasAuthority('READ_CITY')")
    public ResponseEntity<CityDto> getCityById(@PathVariable Long id) {
        CityDto cityDto = cityService.findById(id);
        return ResponseEntity.ok(cityDto);
    }

    @GetMapping("/city/name/{name}")
    @PreAuthorize("hasAuthority('READ_CITY')")
    public ResponseEntity<CityDto> getCityByName(@PathVariable String name) {
        CityDto cityDto = cityService.findByName(name);
        return ResponseEntity.ok(cityDto);
    }

    @DeleteMapping("/city/{id}")
    @PreAuthorize("hasAuthority('DELETE_CITY') and hasAuthority('READ_CITY')")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        cityService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/city/{id}")
    @PreAuthorize("hasAuthority('CREATE_CITY') and hasAuthority('READ_CITY')")
    public ResponseEntity<CityDto> updateCity(@PathVariable Long id, @RequestBody CityDto cityDto) {
        CityDto updatedCityDto = cityService.update(id, cityDto);
        return ResponseEntity.ok(updatedCityDto);
    }
}
