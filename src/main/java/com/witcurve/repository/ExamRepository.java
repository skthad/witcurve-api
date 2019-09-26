package com.witcurve.repository;

import com.witcurve.domain.Exam;
import com.witcurve.domain.enumeration.ExamStatus;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("select e from Exam e where e.schoolInfo.id = ?1 " +
        "and e.startDate between ?2 and ?3 and e.status in ?4 order by e.startDate desc")
    Page<Exam> findAllBySchoolInfoAndDateRangeAndStatuses(Long schoolInfoId, LocalDate startDate, LocalDate endDate, List<ExamStatus> statusList, Pageable pageable);

    @Query("select distinct gsd.exam from GeneralSlotDetails gsd where gsd.exam.schoolInfo.id = ?1 and gsd.grade in ?2 " +
        "and gsd.exam.startDate between ?3 and ?4 and gsd.exam.status in ?5 order by gsd.exam.startDate desc")
    Page<Exam> findAllBySchoolInfoAndGradeAndDateRangeAndStatuses(Long schoolInfoId, List<Grade> grades, LocalDate startDate, LocalDate endDate, List<ExamStatus> statusList, Pageable pageable);

    @Query("select e from Exam e where e.schoolInfo.id = ?1 " +
        "and e.startDate between ?2 and ?3 order by e.startDate desc")
    Page<Exam> findAllBySchoolInfoAndDateRange(Long schoolInfoId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("select distinct gsd.exam from GeneralSlotDetails gsd where gsd.exam.schoolInfo.id = ?1 and gsd.grade in ?2 " +
        "and gsd.exam.startDate between ?3 and ?4 order by gsd.exam.startDate desc")
    Page<Exam> findAllBySchoolInfoAndGradeAndDateRange(Long schoolInfoId, List<Grade> grade, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("select distinct ecd.gsd.exam from ExamCourseDetails ecd where ecd.course.id in ?1 and ecd.gsd.exam.startDate between ?2 and ?3 order by ecd.gsd.exam.startDate desc")
    Page<Exam> findExamsForCourseIds(Set<Long> courseIds, LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("select distinct ecd.gsd.exam from ExamCourseDetails ecd where ecd.course.id in ?1 and ecd.gsd.exam.startDate between ?2 and ?3 and ecd.gsd.exam.status in ?4 order by ecd.gsd.exam.startDate desc")
    Page<Exam> findExamsForCourseIdsWithStatus(Set<Long> courseIds, LocalDate startDate, LocalDate endDate, List<ExamStatus> statuses, Pageable pageable);

}

