package com.salsel.repository;

import com.salsel.model.Ticket;
import com.salsel.model.Account;
import com.salsel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String username);
    @Modifying
    @Query("UPDATE User u SET u.status = false WHERE u.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.status = true WHERE u.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.password = :pass WHERE u.id = :id")
    void updatePassword(@Param("id") Long id, @Param("pass") String pass);

    @Query("SELECT u FROM User u WHERE u.status = :status ORDER BY u.id DESC")
    List<User> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.status = true")
    User findByIdWhereStatusIsTrue(@Param("id") Long id);
    @Query("SELECT u FROM User u WHERE u.status = true ORDER BY u.id DESC")
    List<User> findAllInDesOrderByIdAndStatus();

    Optional<User> findByEmail(String userEmail);
    Optional<User> findByEmailAndStatusIsTrue(String email);
    Optional<User> findByNameAndStatusIsTrue(String name);
}
