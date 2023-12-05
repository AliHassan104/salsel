package com.salsel.repository;

import com.salsel.model.Department;
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
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);

    @Modifying
    @Query("UPDATE Department d SET d.status = false WHERE d.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Query("SELECT d FROM Department d WHERE d.status = true ORDER BY d.id DESC")
    List<Department> findAllInDesOrderByIdAndStatus();

    @Query("SELECT d FROM Department d WHERE d.status = true ORDER BY d.id DESC")
    Page<Department> findAllInDesOrderByIdAndStatus(Pageable page);

    @Query("SELECT d FROM Department d WHERE d.id = :id AND d.status = true")
    Department findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT d FROM Department d WHERE d.name LIKE %:searchName% AND d.status = true")
    Page<Department> findDepartmentsByName(@Param("searchName") String searchName, Pageable page);
}
