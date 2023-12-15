package com.salsel.repository;

import com.salsel.model.ProductField;
import com.salsel.model.ProductFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFieldValuesRepository extends JpaRepository<ProductFieldValues, Long> {
    List<ProductFieldValues> findByproductField_Id(Long productFieldId);
    ProductFieldValues findByName(String name);
    ProductFieldValues findByProductFieldAndName(ProductField productField, String name);
    List<ProductFieldValues> findByStatus(String status);
    @Query("SELECT pf FROM ProductFieldValues pf WHERE pf.id = :id AND pf.status = Active")
    ProductFieldValues findByIdWhereStatusIsTrue(@Param("id") Long id);
}
