package com.witcurve.repository;

import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExamCourseDetailsRepository extends JpaRepository<ExamCourseDetails, Long> {

    @Query("Select ecd from ExamCourseDetails ecd where ecd.gsd.grade=?1 and ecd.gsd.exam.id = ?2 order by ecd.date asc, ecd.gsd.start asc")
    List<ExamCourseDetails> findByGradeAndExamIdOrderByGsdStart(Grade grade, Long examId);

    @Query("Select ecd from ExamCourseDetails ecd where ecd.gsd.grade=?1 and ecd.date between ?2 and ?3 order by ecd.date asc, ecd.gsd.start asc")
    List<ExamCourseDetails> findByGradeBetweenDatesOrderByGsdStart(Grade grade, LocalDate fromDate, LocalDate toDate);

    @Query("Select ecd from ExamCourseDetails ecd where ecd.course.id in ?1 and ecd.date between ?2 and ?3 order by ecd.date asc, ecd.gsd.start asc")
    List<ExamCourseDetails> findByCoursesBetweenDateOrderByGsdStart(List<Long> courseId, LocalDate fromDate, LocalDate toDate);

    @Modifying
    @Query("Delete from ExamCourseDetails ecd where ecd.gsd.exam.id=?1")
    void deleteByExamId(Long examId);

}
