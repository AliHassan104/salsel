package com.salsel.repository;

import com.salsel.model.City;
import com.salsel.model.Pricing;
import com.salsel.model.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatesRepository extends JpaRepository<Rates, Long> {

    @Modifying
    @Query("UPDATE Rates r SET r.status = false WHERE r.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Rates r SET r.status = true WHERE r.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT r FROM Rates r WHERE r.status = :status ORDER BY r.id DESC")
    List<Rates> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    Optional<Rates> findByFromCountryAndToCountryAndProductAndWeightRangeFromLessThanEqualAndWeightRangeToGreaterThanEqual(
            String fromCountry, String toCountry, String product, Double weightRangeFrom, Double weightRangeTo);

    Optional<Rates> findByFromCountryAndToCountryAndProduct(String fromCountry, String toCountry, String product);
}
