package com.salsel.service.impl;

import com.salsel.dto.PricingDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Pricing;
import com.salsel.repository.PricingRepository;
import com.salsel.service.PricingService;
import org.springframework.stereotype.Service;

@Service
public class PricingServiceImpl implements PricingService {

    private final PricingRepository pricingRepository;

    public PricingServiceImpl(PricingRepository pricingRepository) {
        this.pricingRepository = pricingRepository;
    }

    private final static String SALSEL_INTERNATIONAL_INBOUND_PARCEL = "SALSEL International Inbound Parcel";
    private final static String SALSEL_INTERNATIONAL_PARCEL = "SALSEL International Parcel";


    @Override
    public Double getPrice(String fromCountry, String toCountry, String product, Double productWeight) {

        if ((SALSEL_INTERNATIONAL_INBOUND_PARCEL.equals(product) || SALSEL_INTERNATIONAL_PARCEL.equals(product)) && productWeight > 2.5) {
            throw new RecordNotFoundException("Product weight exceeds the allowed limit of 2.5 kg for the specified product.");
        }

        Pricing pricing = pricingRepository.findByCountryAndProduct(toCountry, product);

        if (pricing != null) {
            double weightDifference = productWeight - pricing.getWeightRangeFrom();
            int extraChargesCount = (int) Math.ceil(weightDifference / 0.5);
            return pricing.getCharges() + (extraChargesCount * pricing.getAdditionalCharges());
        }
        else {
            throw new RecordNotFoundException("An error occurred while calculating price.");
        }
    }

    public PricingDto toDto(Pricing pricing) {
        return PricingDto.builder()
                .id(pricing.getId())
                .country(pricing.getCountry())
                .product(pricing.getProduct())
                .weightRangeFrom(pricing.getWeightRangeFrom())
                .weightRangeTo(pricing.getWeightRangeTo())
                .charges(pricing.getCharges())
                .code(pricing.getCode())
                .additionalCharges(pricing.getAdditionalCharges())
                .build();
    }

    public Pricing toEntity(PricingDto pricingDto) {
        return Pricing.builder()
                .id(pricingDto.getId())
                .country(pricingDto.getCountry())
                .product(pricingDto.getProduct())
                .weightRangeFrom(pricingDto.getWeightRangeFrom())
                .weightRangeTo(pricingDto.getWeightRangeTo())
                .charges(pricingDto.getCharges())
                .code(pricingDto.getCode())
                .additionalCharges(pricingDto.getAdditionalCharges())
                .build();
    }
}
