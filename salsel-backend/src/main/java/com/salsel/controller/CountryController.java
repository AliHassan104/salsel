package com.salsel.controller;

import com.salsel.dto.CountryDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CountryController {
    private final CountryService countryService;
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/country")
    @PreAuthorize("hasAuthority('CREATE_COUNTRY') and hasAuthority('READ_COUNTRY')")
    public ResponseEntity<CountryDto> createCountry(@RequestBody CountryDto countryDto) {
        return ResponseEntity.ok(countryService.save(countryDto));
    }

    @GetMapping("/country")
    @PreAuthorize("hasAuthority('READ_COUNTRY')")
    public ResponseEntity<List<CountryDto>> getAllCountry() {
        List<CountryDto> countryDtoList = countryService.getAll();
        return ResponseEntity.ok(countryDtoList);
    }

    @GetMapping("/country/page")
    @PreAuthorize("hasAuthority('READ_COUNTRY')")
    public ResponseEntity<PaginationResponse> getAllPaginatedCountry(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {
        PaginationResponse paginationResponse = countryService.getAllPaginatedCountry(pageNumber, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/country/{id}")
    @PreAuthorize("hasAuthority('READ_COUNTRY')")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable Long id) {
        CountryDto countryDto = countryService.findById(id);
        return ResponseEntity.ok(countryDto);
    }

    @GetMapping("/country/name/{name}")
    @PreAuthorize("hasAuthority('READ_COUNTRY')")
    public ResponseEntity<CountryDto> getCountryByName(@PathVariable String name) {
        CountryDto countryDto = countryService.findByName(name);
        return ResponseEntity.ok(countryDto);
    }

    @GetMapping("/country/names/{name}")
    @PreAuthorize("hasAuthority('READ_COUNTRY')")
    public ResponseEntity<PaginationResponse> getAllCountryByName(
            @PathVariable String name,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "15", required = false) Integer pageSize
    ) {
        PaginationResponse paginationResponse = countryService.searchByName(name, pageNumber, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @DeleteMapping("/country/{id}")
    @PreAuthorize("hasAuthority('DELETE_COUNTRY') and hasAuthority('READ_COUNTRY')")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
        countryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/country/{id}")
    @PreAuthorize("hasAuthority('CREATE_COUNTRY') and hasAuthority('READ_COUNTRY')")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable Long id, @RequestBody CountryDto countryDto) {
        CountryDto updatedCountryDto = countryService.update(id, countryDto);
        return ResponseEntity.ok(updatedCountryDto);
    }
}
