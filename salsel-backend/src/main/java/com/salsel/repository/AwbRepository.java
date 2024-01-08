package com.salsel.repository;

import com.salsel.model.Awb;
import com.salsel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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



//    @Query("SELECT MAX(a.uniqueNumber) FROM Awb a")
//    Long findMaxUniqueNumber();

}
