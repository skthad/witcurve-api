package com.witcurve.repository;

import com.witcurve.domain.Exam;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("select e from Exam e where e.schoolInfo.id = ?1 and e.grade = ?2 " +
        "and e.startDate between ?3 and ?4 order by startDate desc")
    List<Exam> findAllBySchoolInfoAndGradeAndDateRange(Long schoolInfoId, Grade grade, LocalDate startDate, LocalDate endDate);

    @Query("select e from Exam e where e.schoolInfo.id = ?1 and e.grade = ?2 " +
        "and e.startDate between ?3 and ?4 and e.status = ?5 order by startDate desc")
    List<Exam> findAllBySchoolInfoAndGradeAndDateRange(Long schoolInfoId, Grade grade, LocalDate startDate, LocalDate endDate, ExamStatus status);

    @Query("select e from Exam e where e.schoolInfo.id = ?1 " +
        "and e.startDate between ?2 and ?3 order by startDate desc")
    List<Exam> findAllBySchoolInfoAndDateRange(Long schoolInfoId, LocalDate startDate, LocalDate endDate);

    @Query("select e from Exam e where e.schoolInfo.id = ?1 and e.grade = ?2 and e.status = 'PUBLISHED' " +
        "and (e.startDate between ?3 and ?4 or e.endDate between ?3 and ?4)")
    List<Long> findOverlappingExams(Long schoolInfoId, Grade grade, LocalDate startDate, LocalDate endDate);

    @Query("select e from Exam e where e.schoolInfo.id = ?1 and e.grade = ?2 and e.status = 'PUBLISHED' " +
        "and (e.startDate between ?3 and ?4 or e.endDate between ?3 and ?4) and e.id != ?5")
    List<Long> findOverlappingExams(Long schoolInfoId, Grade grade, LocalDate startDate, LocalDate endDate, Long examId);

}

