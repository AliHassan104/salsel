package com.salsel.repository;

import com.salsel.model.AddressBook;
import com.salsel.model.Awb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {

    @Modifying
    @Query("UPDATE AddressBook ab SET ab.status = false WHERE ab.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(ab) > 0 THEN true ELSE false END FROM AddressBook ab WHERE ab.uniqueId = :uniqueId")
    boolean existsByUniqueId(@Param("uniqueId") String uniqueId);

    @Query("SELECT CASE WHEN COUNT(ab) > 0 THEN true ELSE false END FROM AddressBook ab WHERE ab.uniqueId = :uniqueId AND ab.userType = :userType")
    boolean existsByUniqueIdShipperOrRecipient(@Param("uniqueId") String uniqueId,@Param("userType") String userType);


    @Modifying
    @Query("UPDATE AddressBook ab SET ab.status = true WHERE ab.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT ab FROM AddressBook ab WHERE ab.status = :status ORDER BY ab.id DESC")
    List<AddressBook> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT ab FROM AddressBook ab WHERE ab.userType = :userType AND ab.status = :status ORDER BY ab.id DESC")
    List<AddressBook> findAllInDesOrderByUserTypeAndStatus(@Param("userType") String userType,@Param("status") boolean status);

    @Query("SELECT ab FROM AddressBook ab WHERE ab.id = :id AND ab.status = true")
    AddressBook findByIdWhereStatusIsTrue(@Param("id") Long id);



    @Query("SELECT ab FROM AddressBook ab WHERE ab.status = :status " +
            "AND (:userType IS NULL OR ab.userType = :userType) " +
            "AND ab.createdBy = :createdBy ORDER BY ab.id DESC")
    List<AddressBook> findAllInDesOrderByCreatedByAndStatus(
            @Param("status") boolean status,
            @Param("userType") String userType,
            @Param("createdBy") String createdBy
    );


    @Query("SELECT ab FROM AddressBook ab WHERE ab.status = :status " +
            "AND (:userType IS NULL OR ab.userType = :userType) " +
            "AND ab.accountNumber = :accountNumber ORDER BY ab.id DESC")
    List<AddressBook> findAllInDesOrderByAccountNumberAndStatus(
            @Param("status") boolean status,
            @Param("userType") String userType,
            @Param("accountNumber") String accountNumber
    );

    Optional<AddressBook> findByUniqueId(String uniqueId);
}
