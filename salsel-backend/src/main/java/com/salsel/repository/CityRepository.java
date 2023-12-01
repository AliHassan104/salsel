package com.salsel.repository;

import com.salsel.model.City;
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
public interface CityRepository extends JpaRepository<City,Long> {
    Optional<City> findByName(String name);

    @Modifying
    @Query("UPDATE City c SET c.status = false WHERE c.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Query("SELECT c FROM City c WHERE c.status = true ORDER BY c.id DESC")
    List<City> findAllInDesOrderByIdAndStatus();

    @Query("SELECT c FROM City c WHERE c.country.id = :cityId AND c.status = true")
    List<City> findAllByCountryWhereStatusIsTrue(Long cityId);

    @Query("SELECT c FROM City c WHERE c.status = true ORDER BY c.id DESC")
    Page<City> findAllInDesOrderByIdAndStatus(Pageable page);

    @Query("SELECT c FROM City c WHERE c.name LIKE %:searchName% AND c.status = true")
    Page<City> findCityByName(@Param("searchName") String searchName, Pageable page);
}
