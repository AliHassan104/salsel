package com.salsel.controller;

import com.salsel.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/pricing")
    public ResponseEntity<Double> getPricing(@RequestParam(value = "fromCountry") String fromCountry,
                                             @RequestParam(value = "toCountry") String toCountry,
                                             @RequestParam(value = "product") String product,
                                             @RequestParam(value = "productWeight") Double productWeight){
        Double price = pricingService.getPrice(fromCountry,toCountry,product,productWeight);
        return ResponseEntity.ok(price);
    }
}
