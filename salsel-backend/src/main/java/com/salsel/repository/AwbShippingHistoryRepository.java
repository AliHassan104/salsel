package com.salsel.repository;

import com.salsel.model.Awb;
import com.salsel.model.AwbShippingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AwbShippingHistoryRepository extends JpaRepository<AwbShippingHistory, Long> {
    List<AwbShippingHistory> findByAwbId(Long awbId);
    List<AwbShippingHistory> findByAwbStatusAndTimestampBetween(String status, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<AwbShippingHistory> findByTimestampBetweenAndAwbStatusNotIn(LocalDateTime startDateTime, LocalDateTime endDateTime, List<String> status);
    Optional<AwbShippingHistory> findTop1ByAwbIdOrderByTimestampDesc(Long awbId);
}
