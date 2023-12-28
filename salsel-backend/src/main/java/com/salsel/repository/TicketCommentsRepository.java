package com.salsel.repository;

import com.salsel.model.TicketComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommentsRepository extends JpaRepository<TicketComments, Long> {
    @Modifying
    @Query("UPDATE TicketComments tc SET tc.status = false WHERE tc.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE TicketComments tc SET tc.status = true WHERE tc.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT tc FROM TicketComments tc WHERE tc.status = :status ORDER BY tc.id DESC")
    List<TicketComments> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT tc FROM TicketComments tc WHERE tc.id = :id AND tc.status = true")
    TicketComments findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT tc FROM TicketComments tc WHERE tc.ticket.id = :ticketCommentsId AND tc.status = true")
    List<TicketComments> findAllByTicketCommentsWhereStatusIsTrue(Long ticketCommentsId);
}
