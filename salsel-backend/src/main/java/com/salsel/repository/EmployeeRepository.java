package com.salsel.repository;

import com.salsel.model.Employee;
import com.salsel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Modifying
    @Query("UPDATE Employee e SET e.status = false WHERE e.id = :id")
    void setStatusInactive(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Employee e SET e.status = true WHERE e.id = :id")
    void setStatusActive(@Param("id") Long id);

    @Query("SELECT e FROM Employee e WHERE e.status = :status ORDER BY e.id DESC")
    List<Employee> findAllInDesOrderByIdAndStatus(@Param("status") boolean status);

    @Query("SELECT e FROM Employee e WHERE e.id = :id AND e.status = true")
    Employee findByIdWhereStatusIsTrue(@Param("id") Long id);

    @Query("SELECT e FROM Employee e WHERE e.id = (SELECT MAX(em.id) FROM Employee em)")
    Employee findEmployeeByLatestId();
}
