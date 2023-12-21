package com.salsel.repository;

import com.salsel.model.AwbShippingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwbShippingHistoryRepository extends JpaRepository<AwbShippingHistory, Long> {
}
