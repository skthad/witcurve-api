package com.witcurve.repository;

import com.witcurve.domain.AcademicSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSession, Long> {

    @Query("select a.id from AcademicSession a where a.active = true and a.schoolInfo.school.id = ?1")
    List<Long> getActiveSessionIds(Long schoolId);

    @Modifying
    @Query("update AcademicSession a set a.active = false where a.schoolInfo.id = ?1")
    void deactivateExistingAcademicSessions(Long schoolInfoId);
}
