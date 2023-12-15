package com.salsel.repository;

import com.salsel.model.Account;
import com.salsel.model.ServiceType;
import com.salsel.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    Optional<Ticket> findByShipperName(String name);

    @Modifying
    @Query("UPDATE Ticket t SET t.status = false WHERE t.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Ticket t SET t.status = true WHERE t.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.status = :status ORDER BY t.id DESC")
    List<Ticket> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);
    @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.status = true")
    Ticket findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.status = true ORDER BY t.id DESC")
    List<Ticket> findAllInDesOrderByIdAndStatus();
}
