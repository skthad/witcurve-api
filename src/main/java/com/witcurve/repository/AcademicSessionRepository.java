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

    @Query("select a from AcademicSession a where a.schoolInfo.id = ?1 order by startDate ")
    List<AcademicSession> getAllSessionsInSchoolInfo(Long schoolInfoId);

    @Query("select a from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate = ( " +
        "select max(a.startDate) from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate < ?2)")
    AcademicSession getPreviousSession(Long schoolInfoId, LocalDate sessionStartDate);

    @Query("select a from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate = ( " +
        "select max(a.startDate) from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate <= ?2)")
    AcademicSession nearestSessionToDate(Long schoolInfoId, LocalDate date);

    @Query("select a from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate = ( " +
        "select max(a.startDate) from AcademicSession a where  a.schoolInfo.id = ?1 and a.startDate <= ?2)")
    AcademicSession nearestActiveSessionToDate(Long schoolInfoId, LocalDate date);

    @Query("select a from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate = ( " +
        "select min(a.startDate) from AcademicSession a where a.schoolInfo.id = ?1 and a.startDate > ?2)")
    AcademicSession nextSessionAfterDate(Long schoolInfoId, LocalDate date);

}
