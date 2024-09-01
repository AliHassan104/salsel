package com.salsel.controller;

import com.salsel.dto.RatesDto;
import com.salsel.service.RatesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RatesController {
    private final RatesService ratesService;

    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    @PostMapping("/rates")
    @PreAuthorize("hasAuthority('CREATE_RATES') and hasAuthority('READ_RATES')")
    public ResponseEntity<RatesDto> createRates(@RequestBody RatesDto ratesDto) {
        return ResponseEntity.ok(ratesService.addRates(ratesDto));
    }

    @GetMapping("/rates")
    @PreAuthorize("hasAuthority('READ_RATES')")
    public ResponseEntity<List<RatesDto>> getAllRates(@RequestParam(value = "status") Boolean status) {
        List<RatesDto> ratesDtoList = ratesService.getAll(status);
        return ResponseEntity.ok(ratesDtoList);
    }


    @GetMapping("/rates/{id}")
    @PreAuthorize("hasAuthority('READ_RATES')")
    public ResponseEntity<RatesDto> getRatesById(@PathVariable Long id) {
        RatesDto ratesDto = ratesService.findById(id);
        return ResponseEntity.ok(ratesDto);
    }

    @DeleteMapping("/rates/{id}")
    @PreAuthorize("hasAuthority('DELETE_RATES') and hasAuthority('READ_RATES')")
    public ResponseEntity<Void> deleteRates(@PathVariable Long id) {
        ratesService.deleteRates(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rates/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_RATES') and hasAuthority('READ_RATES')")
    public ResponseEntity<Void> updateRatesStatusToActive(@PathVariable Long id) {
        ratesService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rates/web")
    public ResponseEntity<Double> getRates(@RequestParam(value = "fromCountry") String fromCountry,
                                             @RequestParam(value = "toCountry") String toCountry,
                                             @RequestParam(value = "product") String product,
                                             @RequestParam(value = "productWeight") Double productWeight){
        Double rate = ratesService.getRate(fromCountry,toCountry,product,productWeight);
        return ResponseEntity.ok(rate);
    }

}
