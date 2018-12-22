package com.witcurve.repository;

import com.witcurve.domain.ExamCourseDetails;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamCourseDetailsRepository extends JpaRepository<ExamCourseDetails, Long> {

    @Query("Select ecd from ExamCourseDetails ecd where ecd.gsd.grade=?1 and ecd.gsd.exam.id = ?2 order by ecd.date asc, ecd.gsd.start asc")
    List<ExamCourseDetails> findByGradeOrderByGsdStart(Grade grade, Long examId);

    @Query("Select ecd from ExamCourseDetails ecd where ecd.courseTeacher.teacher.id=?1 and ecd.gsd.exam.id = ?2 order by ecd.date asc, ecd.gsd.start asc")
    List<ExamCourseDetails> findByTeacherIdAndExamIdOrderByGsdStart(Long teacherId, Long examId);

}
