package com.salsel.repository;

import com.salsel.model.TicketSubCategory;
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
public interface TicketSubCategoryRepository extends JpaRepository<TicketSubCategory, Long> {

    Optional<TicketSubCategory> findByName(String name);

    @Modifying
    @Query("UPDATE TicketSubCategory tc SET tc.status = false WHERE tc.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TicketSubCategory tc SET tc.status = true WHERE tc.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT tc FROM TicketSubCategory tc WHERE tc.status = :status ORDER BY tc.id DESC")
    List<TicketSubCategory> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT tc FROM TicketSubCategory tc WHERE tc.ticketCategory.id = :ticketCategoryId AND tc.status = true")
    List<TicketSubCategory> findAllByTicketCategoryWhereStatusIsTrue(Long ticketCategoryId);

    @Query("SELECT tc FROM TicketSubCategory tc WHERE tc.status = true ORDER BY tc.id DESC")
    Page<TicketSubCategory> findAllInDesOrderByIdAndStatus(Pageable page);

    @Query("SELECT tc FROM TicketSubCategory tc WHERE tc.id = :id AND tc.status = true")
    TicketSubCategory  findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT tc FROM TicketSubCategory tc WHERE tc.name LIKE %:searchName% AND tc.status = true")
    Page<TicketSubCategory> findTicketSubCategoryByName(@Param("searchName") String searchName, Pageable page);
}
