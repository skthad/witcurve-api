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

    @Query("select e from Exam e where e.schoolInfo.id = ?1 " +
        "and e.startDate between ?2 and ?3 and e.status in ?4 order by e.startDate desc")
    List<Exam> findAllBySchoolInfoAndDateRangeAndStatuses(Long schoolInfoId, LocalDate startDate, LocalDate endDate, List<ExamStatus> statusList);

    @Query("select distinct gsd.exam from GeneralSlotDetails gsd where gsd.exam.schoolInfo.id = ?1 and gsd.grade=?2 " +
        "and gsd.exam.startDate between ?3 and ?4 and gsd.exam.status in ?5 order by gsd.exam.startDate desc")
    List<Exam> findAllBySchoolInfoAndGradeAndDateRangeAndStatuses(Long schoolInfoId, Grade grade, LocalDate startDate, LocalDate endDate, List<ExamStatus> statusList);

    @Query("select e from Exam e where e.schoolInfo.id = ?1 " +
        "and e.startDate between ?2 and ?3 order by e.startDate desc")
    List<Exam> findAllBySchoolInfoAndDateRange(Long schoolInfoId, LocalDate startDate, LocalDate endDate);

    @Query("select distinct gsd.exam from GeneralSlotDetails gsd where gsd.exam.schoolInfo.id = ?1 and gsd.grade=?2 " +
        "and gsd.exam.startDate between ?3 and ?4 order by gsd.exam.startDate desc")
    List<Exam> findAllBySchoolInfoAndGradeAndDateRange(Long schoolInfoId, Grade grade, LocalDate startDate, LocalDate endDate);


}

