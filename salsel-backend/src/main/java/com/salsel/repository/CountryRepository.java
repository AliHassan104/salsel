package com.salsel.repository;

import com.salsel.model.City;
import com.salsel.model.Account;
import com.salsel.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);

    @Modifying
    @Query("UPDATE Country con SET con.status = false WHERE con.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Country con SET con.status = true WHERE con.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT c FROM Country c WHERE c.status = :status ORDER BY c.id DESC")
    List<Country> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);
    Country findCountryByName(String name);

    @Query("SELECT c FROM Country c WHERE c.status = true ORDER BY c.id DESC")
    Page<Country> findAllInDesOrderByIdAndStatus(Pageable page);

    @Query("SELECT con FROM Country con WHERE con.name LIKE %:searchName% AND con.status = true")
    Page<Country> findCountryByName(@Param("searchName") String searchName, Pageable page);

    @Query("SELECT c FROM Country c WHERE c.id = :id AND c.status = true")
    Country findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT c.code FROM Country c WHERE c.name = :countryName")
    Integer findCountryCodeByCountryName(@Param("countryName") String countryName);
}
