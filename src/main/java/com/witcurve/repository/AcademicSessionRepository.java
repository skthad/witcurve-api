package com.witcurve.repository;

import com.witcurve.domain.AcademicSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSession, Long> {

    @Query("select a.id from AcademicSession a where a.active = true and a.schoolInfo.id = ?1")
    List<Long> getActiveSessionIds(Long schoolInfoId);

    @Query("select a from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate = ( " +
        "select max(a.startDate) from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate < ?2)")
    AcademicSession getPreviousSession(Long schoolInfoId, LocalDate sessionStartDate);

    @Query("select a from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate = ( " +
        "select max(a.startDate) from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate <= ?2)")
    AcademicSession nearestSessionToTerm(Long schoolInfoId, LocalDate termStartDate);

    /*@Query("select a from AcademicSession a where a.schoolInfo.id = ?1 and " +
        "a.startDate >= ?2 order by a.startDate asc limit 1)")
    AcademicSession getNextSession(Long schoolInfoId, LocalDate date);*/

    @Modifying
    @Query("update AcademicSession a set a.active = false where a.schoolInfo.id = ?1")
    void deactivateExistingAcademicSessions(Long schoolInfoId);
}
