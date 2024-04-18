package com.salsel.repository;

import com.salsel.model.EmployeeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAttachmentRepository extends JpaRepository<EmployeeAttachment, Long> {
}
