package com.witcurve.repository;

import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.enumeration.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {

    @Query("select sm from StudentMarks sm where sm.test.id = ?1")
    List<StudentMarks> getByTestId(Long testId);

    @Query("select sm from StudentMarks sm where sm.student.id = ?1 and sm.test.id = ?2")
    List<StudentMarks> getByStudentAndTestId(Long studentId, Long testId);

    @Query("select sm from StudentMarks sm where sm.id = ?1")
    StudentMarks findByStudentMarksId(Long studentMarksId);

    @Query("select sm from StudentMarks sm where sm.test.scd.courseTeacher.id = ?1 and sm.test.type = ?2")
    List<StudentMarks> getByCourseTeacherIdAndEventType(Long courseTeacherId, EventType eventType);

    @Query("select sm from StudentMarks sm where sm.test.courseTeacher.id = ?1 and sm.test.type = ?2")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeForAssignment(Long courseTeacherId, EventType eventType);

    @Query("select sm from StudentMarks sm where sm.test.scd.courseTeacher.id = ?1 and sm.test.type = ?2 and sm.student.id=?3")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeAndStudentId(Long courseTeacherId, EventType eventType,Long studentId);

    @Query("select sm from StudentMarks sm where sm.test.courseTeacher.id = ?1 and sm.test.type = ?2 and sm.student.id=?3")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeAndStudentIdForAssignment(Long courseTeacherId, EventType eventType,Long studentId);

    @Query("select sm from StudentMarks sm where sm.test.courseTeacher.id = ?1 and sm.test.type = ?2 and sm.student.id=?3")
    List<StudentMarks> getByCourseTeacherIdAndEventTypeAndStudentIdForExam(Long courseTeacherId, EventType eventType,Long studentId);

}

