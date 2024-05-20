package com.salsel.repository;

import com.salsel.model.BillingAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingAttachmentRepository extends JpaRepository<BillingAttachment, Long> {
}
