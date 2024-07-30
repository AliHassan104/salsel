package com.salsel.service;

public interface PricingService {
    Double getPrice(String fromCountry, String toCountry, String product, Double productWeight);
}
