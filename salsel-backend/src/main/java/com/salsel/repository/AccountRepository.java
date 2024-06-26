package com.salsel.repository;

import com.salsel.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    @Modifying
    @Query("UPDATE Account ac SET ac.status = false WHERE ac.id = :id")
    void setStatusInactive(@Param("id") Long id);
    @Modifying
    @Query("UPDATE Account ac SET ac.status = true WHERE ac.id = :id")
    void setStatusActive(@Param("id") Long id);
    @Query("SELECT ac FROM Account ac WHERE ac.status = :status ORDER BY ac.id DESC")
    List<Account> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);
    @Query("SELECT a FROM Account a WHERE a.id = :id AND a.status = true")
    Account findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT MAX(CAST(a.accountNumber AS long)) FROM Account a")
    Long findMaxAccountNumber();

    Account findByAccountNumber(Long accountNumber);

    @Query("SELECT a FROM Account a WHERE a.id = (SELECT MAX(b.id) FROM Account b)")
    Account findAccountByLatestId();

    List<Account> findAllByCreatedAtBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM Account a WHERE a.status = :status And a.email = :email ORDER BY a.id DESC")
    List<Account> findAllInDesOrderByEmailAndStatus(@Param("status") boolean status, @Param("email") String email);

    @Query("SELECT MIN(a.createdAt) FROM Account a")
    LocalDate findMinCreatedAt();

    @Query("SELECT MAX(a.createdAt) FROM Account a")
    LocalDate findMaxCreatedAt();

    @Query("SELECT COUNT(a) FROM Account a WHERE a.status = :status")
    Long countByStatus(@Param("status") Boolean status);

    @Query("SELECT COUNT(*) FROM Account a\n" +
            "WHERE a.status = :status\n" +
            "  AND (a.email = :email)")
    Long countByStatusAndEmail(boolean status, String email);


    @Query("SELECT a.accountNumber FROM Account a WHERE a.status = :status ORDER BY a.id DESC")
    List<Long> findAccountNumbersByStatus(@Param("status") boolean status);
}
