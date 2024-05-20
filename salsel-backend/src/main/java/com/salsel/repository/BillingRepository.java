package com.salsel.repository;

import com.salsel.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing,Long> {

    @Query("SELECT b FROM Billing b WHERE b.status = :status ORDER BY b.id DESC")
    List<Billing> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT b FROM Billing b WHERE billingStatus != 'Closed'")
    List<Billing> getAllBillingsWhereStatusIsNotClosed();

    @Query("SELECT b FROM Billing b WHERE b.id = (SELECT MAX(e.id) FROM Billing e)")
    Optional<Billing> findBillingByLatestId();
}
