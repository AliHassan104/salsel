package com.salsel.repository;

import com.salsel.model.AwbStatusComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwbStatusCommentRepository extends JpaRepository<AwbStatusComment, Long> {
    List<AwbStatusComment> findByAwbId(Long awbId);
}
