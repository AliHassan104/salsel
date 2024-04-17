package com.salsel.repository;

import com.salsel.model.Awb;
import com.salsel.model.Ticket;
import com.salsel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Query("SELECT t FROM Ticket t WHERE t.ticketStatus = :status ORDER BY t.id DESC")
    List<Ticket> findAllInDesOrderByIdAndTicketStatus(@Param("status") String status);

    @Query("SELECT t FROM Ticket t WHERE t.id = :id AND t.status = true")
    Ticket findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t WHERE t.status = true ORDER BY t.id DESC")
    List<Ticket> findAllInDesOrderByIdAndStatus();

    @Query("SELECT COUNT(t) FROM Ticket t")
    Long countByLoggedInUser();

    @Query("SELECT t FROM Ticket t WHERE t.status = :status And t.createdBy = :createdBy ORDER BY t.id DESC")
    List<Ticket> findAllInDesOrderByEmailAndStatus(@Param("status") boolean status, @Param("createdBy") String createdBy);

    List<Ticket> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Ticket t WHERE (t.assignedTo = :assignedTo OR t.createdBy = :createdBy OR t.email = :email) AND (t.createdAt BETWEEN :startDate AND :endDate) ORDER BY t.id DESC")
    List<Ticket> findAllByCreatedAtBetweenAndLoggedInUser(@Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate, @Param("createdBy") String createdBy, @Param("assignedTo") String assignedTo, @Param("email") String email);

    @Query("SELECT t FROM Ticket t WHERE (t.status = :status AND t.createdBy = :createdBy) OR (t.status = :status AND t.assignedTo = :assignedTo) OR (t.status = :status AND t.email = :email) ORDER BY t.id DESC")
    List<Ticket> findAllInDesOrderByCreatedByOrAssignedToAndStatus(@Param("status") boolean status, @Param("createdBy") String createdBy, @Param("assignedTo") String assignedTo, @Param("email") String email);

    @Query("SELECT MIN(t.createdAt) FROM Ticket t")
    LocalDate findMinCreatedAt();

    @Query("SELECT MAX(t.createdAt) FROM Ticket t")
    LocalDate findMaxCreatedAt();

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = :status")
    Long countByStatus(@Param("status") Boolean status);

    @Query("SELECT COUNT(*) FROM Ticket t\n" +
            "WHERE t.status = :status\n" +
            "  AND (t.createdBy = :createdBy OR t.assignedTo = :assignedTo)")
    Long countByStatusAndCreatedByOrAssignedTo(boolean status, String createdBy, String assignedTo);

    @Query("SELECT t FROM Ticket t WHERE ticketStatus != 'Closed'")
    List<Ticket> getAllTicketsWhereStatusIsNotClosed();
}
