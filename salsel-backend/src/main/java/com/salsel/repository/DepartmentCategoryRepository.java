package com.salsel.repository;

import com.salsel.model.City;
import com.salsel.model.Country;
import com.salsel.model.Account;
import com.salsel.model.DepartmentCategory;
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
public interface DepartmentCategoryRepository extends JpaRepository<DepartmentCategory, Long> {
    Optional<DepartmentCategory> findByName(String name);

    @Modifying
    @Query("UPDATE DepartmentCategory dc SET dc.status = false WHERE dc.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE DepartmentCategory dc SET dc.status = true WHERE dc.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT dc FROM DepartmentCategory dc WHERE dc.status = :status ORDER BY dc.id DESC")
    List<DepartmentCategory> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT dc FROM DepartmentCategory dc WHERE dc.department.id = :departmentCategoryId AND dc.status = true")
    List<DepartmentCategory> findAllByDepartmentWhereStatusIsTrue(Long departmentCategoryId);

    @Query("SELECT dc FROM DepartmentCategory dc WHERE dc.status = true ORDER BY dc.id DESC")
    Page<DepartmentCategory> findAllInDesOrderByIdAndStatus(Pageable page);

    @Query("SELECT dc FROM DepartmentCategory dc WHERE dc.id = :id AND dc.status = true")
    DepartmentCategory  findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT dc FROM DepartmentCategory dc WHERE dc.name LIKE %:searchName% AND dc.status = true")
    Page<DepartmentCategory> findDepartmentCategoryByName(@Param("searchName") String searchName, Pageable page);
}
