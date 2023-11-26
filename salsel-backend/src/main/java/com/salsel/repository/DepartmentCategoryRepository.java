package com.salsel.repository;

import com.salsel.model.DepartmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentCategoryRepository extends JpaRepository<DepartmentCategory, Long> {
}
