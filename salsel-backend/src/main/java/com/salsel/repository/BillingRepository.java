package com.salsel.repository;

import com.salsel.model.Account;
import com.salsel.model.Billing;
import com.salsel.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
