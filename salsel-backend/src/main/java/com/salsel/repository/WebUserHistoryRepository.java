package com.salsel.repository;

import com.salsel.model.WebUserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebUserHistoryRepository extends JpaRepository<WebUserHistory, Long> {
}
