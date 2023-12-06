package com.salsel.repository;

import com.salsel.model.ProductField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductFieldRepository extends JpaRepository<ProductField, Long> {
    List<ProductField> findByProductFieldValuesList_Id(Long productFieldValueId);
    List<ProductField> findAllByStatusOrderBySequenceAsc(String Status);
    ProductField findByName(String name);
    @Query("SELECT pf FROM ProductField pf WHERE pf.id = :id AND pf.status = Active")
    ProductField findByIdWhereStatusIsTrue(@Param("id") Long id);
    @Query("SELECT pf FROM ProductField pf WHERE pf.name LIKE %:searchName%")
    List<ProductField> findProductFieldsByName(@Param("searchName") String searchName);
    @Modifying
    @Transactional
    @Query("UPDATE ProductField pf SET pf.status = 'inActive' WHERE pf.id = :id")
    void setStatusInactive(@Param("id") Long id);
}
