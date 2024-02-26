package com.salsel.repository;

import com.salsel.model.Account;
import com.salsel.model.Awb;
import com.salsel.model.Ticket;
import com.salsel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AwbRepository extends JpaRepository<Awb, Long> {
    @Modifying
    @Query("UPDATE Awb a SET a.status = false WHERE a.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Awb a SET a.status = true WHERE a.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT a FROM Awb a WHERE a.status = :status ORDER BY a.id DESC")
    List<Awb> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT a FROM Awb a WHERE a.id = :awbId AND a.status = true")
    Awb findByIdWhereStatusIsTrue(@Param("awbId") Long awbId);

    @Query("SELECT a FROM Awb a WHERE a.status = true And a.emailFlag = false")
    List<Awb> findAllWhereStatusIsTrueAndEmailFlagIsFalse();

    @Query("SELECT MAX(CAST(a.uniqueNumber AS long)) FROM Awb a")
    Long findMaxUniqueNumber();

    @Query("SELECT a FROM Awb a WHERE a.status = :status ORDER BY a.id DESC")
    Awb findLatestRecord(@Param("status") boolean status);

    @Query("SELECT a FROM Awb a WHERE a.status = :status And a.createdBy = :createdBy ORDER BY a.id DESC")
    List<Awb> findAllInDesOrderByEmailAndStatus(@Param("status") boolean status, @Param("createdBy") String createdBy);

    @Query("SELECT a FROM Awb a WHERE a.status = :status And a.assignedToUser = :user")
    List<Awb> findAllAwbByAssignedUserAndStatus(@Param("status") boolean status, @Param("user") String user);

    @Query("SELECT a FROM Awb a WHERE a.status = :status And a.assignedTo = :assignedTo ORDER BY a.id DESC")
    List<Awb> findAllInDesOrderByRoleAndStatus(@Param("status") boolean status, @Param("assignedTo") String assignedTo);

    @Query("SELECT a FROM Awb a WHERE (a.status = :status AND a.createdBy = :createdBy) OR (a.status = :status AND a.assignedTo = :assignedTo) ORDER BY a.id DESC")
    List<Awb> findAllInDesOrderByCreatedByOrAssignedToAndStatus(@Param("status") boolean status, @Param("createdBy") String createdBy, @Param("assignedTo") String assignedTo);

    Optional<Awb> findByUniqueNumber(Long uniqueNumber);

    @Query("SELECT COUNT(a) FROM Awb a WHERE a.status = :status AND a.awbStatus = :awbStatus")
    Long countByStatusAndAwbStatus(@Param("status") boolean status, @Param("awbStatus") String awbStatus);

    @Query("SELECT COUNT(a) FROM Awb a WHERE a.status = :status")
    Long countByStatus(@Param("status") Boolean status);

    List<Awb> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT a FROM Awb a WHERE (a.assignedTo = :assignedTo OR a.createdBy = :createdBy) AND (a.createdAt BETWEEN :startDate AND :endDate) ORDER BY a.id DESC")
    List<Awb> findAllByCreatedAtBetweenAndLoggedInUser(@Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate, @Param("createdBy") String createdBy, @Param("assignedTo") String assignedTo);


    @Query("SELECT COUNT(*) FROM Awb a\n" +
            "WHERE a.status = :status\n" +
            "  AND a.awbStatus = :awbStatus\n" +
            "  AND (a.createdBy = :createdBy OR a.assignedTo = :assignedTo)")
    Long countByStatusAndAwbStatusAndCreatedByOrAssignedTo(boolean status, String awbStatus, String createdBy, String assignedTo);

    @Query("SELECT COUNT(*) FROM Awb a\n" +
            "WHERE a.status = :status\n" +
            "  AND (a.createdBy = :createdBy OR a.assignedTo = :assignedTo)")
    Long countByStatusAndCreatedByOrAssignedTo(boolean status, String createdBy, String assignedTo);

    @Query("SELECT MIN(a.createdAt) FROM Awb a")
    LocalDate findMinCreatedAt();

    @Query("SELECT MAX(a.createdAt) FROM Awb a")
    LocalDate findMaxCreatedAt();

//    @Query("SELECT MAX(a.uniqueNumber) FROM Awb a")
//    Long findMaxUniqueNumber();

}
