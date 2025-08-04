package com.studyswap.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studyswap.backend.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
}
