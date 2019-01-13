package com.witcurve.repository;

import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.enumeration.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {

    @Query("select sm from StudentMarks sm where sm.event.id = ?1 order by sm.event.date desc")
    List<StudentMarks> getByTestId(Long eventId);

    @Query("select sm from StudentMarks sm where sm.student.id = ?1 and sm.event.id = ?2 order by sm.event.date desc")
    List<StudentMarks> getByStudentAndTestId(Long studentId, Long eventId);

    @Query("select sm from StudentMarks sm where sm.id = ?1")
    StudentMarks findByStudentMarksId(Long studentMarksId);

    @Query("select sm from StudentMarks sm where sm.student.id=?1 and sm.examCourseDetails.id = ?2")
    List<StudentMarks> getByStudentIdAndExamCourseDetailsId(Long studentId ,Long examCourseDetailsId);

    @Query("select sm from StudentMarks sm where sm.event.id = ?1 order by sm.event.date desc")
    List<StudentMarks> getAllStudentsForTestOrAssignmentForACourseTeacher(Long eventId);

    @Query("select sm from StudentMarks sm where sm.event.scd.courseTeacher.id = ?1 and sm.event.id = ?2 order by sm.event.date desc")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeForTest(Long courseTeacherId, Long eventId);

    @Query("select sm from StudentMarks sm where sm.event.courseTeacher.id = ?1 and sm.event.id = ?2 order by sm.event.date desc")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeForAssignment(Long courseTeacherId, Long eventId);

    @Query("select sm from StudentMarks sm where sm.event.scd.courseTeacher.id = ?1 and sm.event.type = ?2 and sm.student.id=?3 order by sm.event.date desc")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeAndStudentId(Long courseTeacherId, EventType eventType,Long studentId);

    @Query("select sm from StudentMarks sm where sm.event.courseTeacher.id = ?1 and sm.event.type = ?2 and sm.student.id=?3 order by sm.event.date desc")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeAndStudentIdForAssignment(Long courseTeacherId, EventType eventType,Long studentId);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.id = ?1")
    List<StudentMarks> getStudentMarksForCourseTeacherIdByEcdIdForExam(Long examCourseDetailsId);

    @Query("select sm from StudentMarks sm where sm.examCourseDetails.courseTeacher.id = ?1 and sm.student.id =?2")
    List<StudentMarks> getStudentMarksForExamByStudentIdAndCourseTeacherId(Long courseTeacherId , Long studentId);

}

