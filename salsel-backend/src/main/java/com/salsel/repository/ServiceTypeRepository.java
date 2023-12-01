package com.salsel.repository;

import com.salsel.model.City;
import com.salsel.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long> {
    @Modifying
    @Query("UPDATE ServiceType st SET st.status = false WHERE st.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Query("SELECT st FROM ServiceType st WHERE st.status = true ORDER BY st.id DESC")
    List<ServiceType> findAllInDesOrderByIdAndStatus();

    Optional<ServiceType> findByCode(String code);

    @Query("SELECT st FROM ServiceType st WHERE st.productType.id = :serviceTypeId AND st.status = true")
    List<ServiceType> findAllByProductTypeWhereStatusIsTrue(Long serviceTypeId);
}
