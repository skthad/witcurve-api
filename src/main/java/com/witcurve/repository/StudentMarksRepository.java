package com.witcurve.repository;

import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {

    @Query("select sm from StudentMarks sm where sm.event.id = ?1")
    List<StudentMarks> getStudentMarksByEventId(Long eventId);

    @Query("select sm from StudentMarks sm where sm.event.id in ?1")
    List<StudentMarks> getStudentMarksByEventId(List<Long> eventIds);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.gsd.exam.id = ?1 and sm.examCourseDetails.gsd.marksPublished in ?2 order by sm.examCourseDetails.date")
    List<StudentMarks> getStudentMarksByExamId(Long examId, List<Boolean> publishList);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.id = ?1 and sm.examCourseDetails.gsd.marksPublished in ?2")
    List<StudentMarks> getStudentMarksByEcdId(Long ecdId, List<Boolean> publishList);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.gsd.exam.id = ?1 " +
        "and sm.student.id in (select student.id from StudentStandard where active is true and standard.id = ?2) and sm.examCourseDetails.gsd.marksPublished in ?3")
    List<StudentMarks> getStudentMarksByExamIdAndStandardId(Long examId, Long standardId, List<Boolean> publishList);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.id = ?1 " +
        "and sm.student.id in (select student.id from StudentStandard where active is true and standard.id = ?2) and sm.examCourseDetails.gsd.marksPublished in ?3")
    List<StudentMarks> getStudentMarksByEcdIdAndStandardId(Long ecdId, Long standardId, List<Boolean> publishList);

    @Query("select sm from StudentMarks sm where sm.event.scd.courseTeacher.course.id = ?1 " +
        "and sm.student.id=?2 and sm.event.type = ?3 " +
        "and sm.event.date between ?4 and ?5 order by sm.event.date asc")
    List<StudentMarks> getByCourseIdAndStudentIdForEvent(Long courseId, Long studentId, EventType type, LocalDate startDate, LocalDate endDate);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.course.id = ?1 " +
        "and sm.student.id =?2 and sm.examCourseDetails.gsd.marksPublished in ?5 and sm.examCourseDetails.date between ?3 and ?4 order by sm.examCourseDetails.date asc")
    List<StudentMarks> getByCourseIdAndStudentIdForExam(Long courseId, Long studentId, LocalDate startDate, LocalDate endDate, List<Boolean> publishList);

    @Query("select sm from StudentMarks sm where sm.event.courseTeacher.course.id = ?1 " +
        "and sm.event.standard.grade=?2 and sm.event.type = ?3 " +
        "and sm.event.date between ?4 and ?5 order by sm.event.date asc")
    List<StudentMarks> getByCourseIdAndGradeForEvent(Long courseId, Grade grade, EventType type, LocalDate startDate, LocalDate endDate);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.course.id = ?1 " +
        "and sm.examCourseDetails.gsd.grade = ?2 and sm.examCourseDetails.gsd.marksPublished in ?5 and sm.examCourseDetails.date between ?3 and ?4 order by sm.examCourseDetails.date asc")
    List<StudentMarks> getByCourseIdAndGradeForExam(Long courseId, Grade grade, LocalDate startDate, LocalDate endDate, List<Boolean> publishList);

    @Query("select sm from StudentMarks sm where sm.event.scd.courseTeacher.course.id = ?1 " +
        "and sm.event.standard.id = ?2 and sm.event.type = ?3 " +
        "and sm.event.date between ?4 and ?5 order by sm.event.date asc")
    List<StudentMarks> getByCourseIdAndStandardForEvent(Long courseId, Long standardId, EventType type, LocalDate startDate, LocalDate endDate);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.gsd.exam.id = ?1 " +
        "and sm.student.id =?2 and sm.examCourseDetails.gsd.marksPublished in ?3 order by sm.examCourseDetails.date asc")
    List<StudentMarks> getByStudentIdForExam(Long examId, Long studentId, List<Boolean> publishList);

}

