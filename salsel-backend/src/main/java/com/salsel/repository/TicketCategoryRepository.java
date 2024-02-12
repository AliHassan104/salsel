package com.salsel.repository;

import com.salsel.model.TicketCategory;
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
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    Optional<TicketCategory> findByName(String name);

    @Modifying
    @Query("UPDATE TicketCategory tc SET tc.status = false WHERE tc.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TicketCategory tc SET tc.status = true WHERE tc.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT tc FROM TicketCategory tc WHERE tc.status = :status ORDER BY tc.id DESC")
    List<TicketCategory> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT tc FROM TicketCategory tc WHERE tc.departmentCategory.id = :departmentCategoryId AND tc.status = true")
    List<TicketCategory> findAllByDepartmentCategoryWhereStatusIsTrue(Long departmentCategoryId);

    @Query("SELECT tc FROM TicketCategory tc WHERE tc.status = true ORDER BY tc.id DESC")
    Page<TicketCategory> findAllInDesOrderByIdAndStatus(Pageable page);

    @Query("SELECT tc FROM TicketCategory tc WHERE tc.id = :id AND tc.status = true")
    TicketCategory  findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT tc FROM TicketCategory tc WHERE tc.name LIKE %:searchName% AND tc.status = true")
    Page<TicketCategory> findDepartmentCategoryByName(@Param("searchName") String searchName, Pageable page);
}
