package com.salsel.repository;

import com.salsel.model.BillingAttachment;
import com.salsel.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
    @Query("SELECT s FROM Statement s WHERE s.accountNumber = :accountNumber AND s.url IS NULL ORDER BY s.createdAt DESC")
    List<Statement> findAllByAccountNumberAndUrlNull(@Param("accountNumber") Long accountNumber);
    List<Statement> findAllByIsEmailSendFalse();
    Optional<Statement> findFirstByAccountNumberOrderByCreatedAtDesc(Long accountNumber);
    List<Statement> findAllByAccountNumber(Long accountNumber);

}
