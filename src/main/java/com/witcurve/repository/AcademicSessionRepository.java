package com.witcurve.repository;

import com.witcurve.domain.AcademicSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSession, Long> {

    @Modifying
    @Query("update AcademicSession a set a.active = false where a.school.id = ?1")
    void deactivateExistingAcademicSessions(Long schoolId);
}
