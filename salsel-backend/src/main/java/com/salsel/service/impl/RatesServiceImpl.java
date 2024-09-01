package com.salsel.service.impl;

import com.salsel.dto.RatesDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Pricing;
import com.salsel.model.Rates;
import com.salsel.repository.RatesRepository;
import com.salsel.service.RatesService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RatesServiceImpl implements RatesService {

    private final static String SALSEL_INTERNATIONAL_INBOUND_PARCEL = "SALSEL International Inbound Parcel";
    private final static String SALSEL_INTERNATIONAL_PARCEL = "SALSEL International Parcel";

    private final RatesRepository ratesRepository;

    public RatesServiceImpl(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
    }

    @Override
    @Transactional
    public RatesDto addRates(RatesDto ratesDto) {
        // Check if rates for the same product, fromCountry, and toCountry already exist within the weight range
        Optional<Rates> existingRate = ratesRepository.findByFromCountryAndToCountryAndProductAndWeightRangeFromLessThanEqualAndWeightRangeToGreaterThanEqual(
                ratesDto.getFromCountry(), ratesDto.getToCountry(), ratesDto.getProduct(), ratesDto.getWeightRangeFrom(), ratesDto.getWeightRangeTo());

        if (existingRate.isPresent()) {
            throw new IllegalArgumentException("Rate already exists for the specified product and weight range.");
        }

        Rates rates = toEntity(ratesDto);
        rates.setStatus(true);
        return toDto(ratesRepository.save(rates));
    }


    @Override
    public Double getRate(String fromCountry, String toCountry, String product, Double productWeight) {
        if ((SALSEL_INTERNATIONAL_INBOUND_PARCEL.equals(product) || SALSEL_INTERNATIONAL_PARCEL.equals(product)) && productWeight > 2.5) {
            throw new RecordNotFoundException("Product weight exceeds the allowed limit of 2.5 kg for the specified product.");
        }

        Optional<Rates> rates = ratesRepository.findByFromCountryAndToCountryAndProductAndWeightRangeFromLessThanEqualAndWeightRangeToGreaterThanEqual(
                fromCountry, toCountry, product, productWeight, productWeight);

        if (rates.isPresent()) {
            Rates presentRate = rates.get();
            return presentRate.getCharges() + calculateAdditionalCharges(presentRate, productWeight);
        } else {
            throw new RecordNotFoundException("Rate not found for the specified criteria.");
        }
    }


    private double calculateAdditionalCharges(Rates rates, Double productWeight) {
        // Calculate the extra charges based on the weight
        if (productWeight > rates.getWeightRangeTo()) {
            double weightDifference = productWeight - rates.getWeightRangeTo();
            int extraChargesCount = (int) Math.ceil(weightDifference / 0.5);
            return extraChargesCount * rates.getAdditionalCharges();
        }
        return 0.0;
    }

    @Override
    public List<RatesDto> getAll(Boolean status) {
        List<Rates> ratesList = ratesRepository.findAllInDesOrderByIdAndStatus(status);
        List<RatesDto> ratesDtoList = new ArrayList<>();

        for (Rates rates : ratesList) {
            RatesDto ratesDto = toDto(rates);
            ratesDtoList.add(ratesDto);
        }
        return ratesDtoList;
    }

    @Override
    public RatesDto findById(Long id) {
        Rates rates = ratesRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Rates not found for id => %d", id)));
        return toDto(rates);
    }

    @Override
    @Transactional
    public RatesDto updateRates(Long id, RatesDto ratesDto) {
        Rates existingRates = ratesRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Rates not found for id => %d", id)));

        existingRates.setCharges(ratesDto.getCharges());
        existingRates.setProduct(ratesDto.getProduct());
        existingRates.setAdditionalCharges(ratesDto.getAdditionalCharges());
        existingRates.setFromCountry(ratesDto.getFromCountry());
        existingRates.setToCountry(ratesDto.getToCountry());
        existingRates.setWeightRangeFrom(ratesDto.getWeightRangeFrom());
        existingRates.setWeightRangeTo(ratesDto.getWeightRangeTo());

        return toDto(ratesRepository.save(existingRates));
    }

    @Override
    @Transactional
    public void deleteRates(Long id) {
        Rates rates = ratesRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Rates not found for id => %d", id)));
        ratesRepository.setStatusInactive(rates.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Rates rates = ratesRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Rates not found for id => %d", id)));
        ratesRepository.setStatusActive(rates.getId());
    }


    public RatesDto toDto(Rates rates) {
        return RatesDto.builder()
                .id(rates.getId())
                .fromCountry(rates.getFromCountry())
                .toCountry(rates.getToCountry())
                .product(rates.getProduct())
                .charges(rates.getCharges())
                .additionalCharges(rates.getAdditionalCharges())
                .weightRangeFrom(rates.getWeightRangeFrom())
                .weightRangeTo(rates.getWeightRangeTo())
                .status(rates.getStatus())
                .build();
    }

    public Rates toEntity(RatesDto ratesDto) {
        return Rates.builder()
                .id(ratesDto.getId())
                .fromCountry(ratesDto.getFromCountry())
                .toCountry(ratesDto.getToCountry())
                .product(ratesDto.getProduct())
                .charges(ratesDto.getCharges())
                .additionalCharges(ratesDto.getAdditionalCharges())
                .weightRangeFrom(ratesDto.getWeightRangeFrom())
                .weightRangeTo(ratesDto.getWeightRangeTo())
                .status(ratesDto.getStatus())
                .build();
    }
}
