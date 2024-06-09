package com.salsel.repository;

import com.salsel.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing,Long> {

    @Modifying
    @Query("UPDATE Billing b SET b.status = false WHERE b.id = :id")
    void setStatusInactive(@Param("id") Long id);
    @Modifying
    @Query("UPDATE Billing b SET b.status = true WHERE b.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT b FROM Billing b WHERE b.status = :status ORDER BY b.id DESC")
    List<Billing> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT b FROM Billing b WHERE billingStatus != 'Closed'")
    List<Billing> getAllBillingsWhereStatusIsNotClosed();

    @Query("SELECT b FROM Billing b WHERE b.id = (SELECT MAX(e.id) FROM Billing e)")
    Optional<Billing> findBillingByLatestId();

    @Query("SELECT b from Billing b WHERE b.customerAccountNumber = :accountNumber")
    List<Billing> findByCustomerAccountNumber(Long accountNumber);

    @Query("SELECT b FROM Billing b WHERE b.invoiceDate <= :givenDate")
    List<Billing> findAllBillingsUpToDate(@Param("givenDate") LocalDate givenDate);

}
