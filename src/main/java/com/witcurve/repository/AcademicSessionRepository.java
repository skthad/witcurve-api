package com.witcurve.repository;

import com.witcurve.domain.AcademicSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSession, Long> {
}
