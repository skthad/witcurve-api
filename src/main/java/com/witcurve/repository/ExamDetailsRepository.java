package com.witcurve.repository;

import com.witcurve.domain.ExamDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamDetailsRepository extends JpaRepository<ExamDetails, Long> {
}

