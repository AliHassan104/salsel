package com.salsel.repository;

import com.salsel.model.Account;
import com.salsel.model.Awb;
import com.salsel.model.ProductFieldValues;
import com.salsel.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType,Long> {
    @Modifying
    @Query("UPDATE ProductType pt SET pt.status = false WHERE pt.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ProductType pt SET pt.status = true WHERE pt.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT pt FROM ProductType pt WHERE pt.status = :status ORDER BY pt.id DESC")
    List<ProductType> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    Optional<ProductType> findByCode(String code);

    @Query("SELECT pt FROM ProductType pt WHERE pt.id = :id AND pt.status = true")
    ProductType findByIdWhereStatusIsTrue(@Param("id") Long id);
}
