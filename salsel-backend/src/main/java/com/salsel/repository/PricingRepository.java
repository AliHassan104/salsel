package com.salsel.repository;

import com.salsel.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    Pricing findByCountryAndProduct(String country, String product);
}
