package com.witcurve.repository;

import com.witcurve.domain.StudentMarks;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, Long> {

    @Query("select sm from StudentMarks sm where sm.event.id = ?1 ")
    List<StudentMarks> getStudentMarksByEventId(Long eventId);

    @Query("select sm from StudentMarks sm where sm.event.id = ?1 and sm.reportCardDesign.id = ?2")
    List<StudentMarks> getStudentMarksByEventIdAndRcdId(Long eventId, Long rcdId);

    @Query("select sm from StudentMarks sm where sm.event.id = ?1 " +
        "and sm.student.id in (select student.id from StudentStandard where active is true and standard.id = ?2)")
    List<StudentMarks> getStudentMarksByEventIdAndStandardId(Long eventId, Long standardId);

    @Query("select sm from StudentMarks sm where sm.event.id in ?1")
    List<StudentMarks> getStudentMarksByEventIds(List<Long> eventIds);

    @Query("select sm from StudentMarks sm where sm.reportCardDesign.exam.id = ?1")
    List<StudentMarks> getStudentMarksByExamId(Long examId);

    @Query("select sm from StudentMarks sm where sm.course.id = ?1")
    List<StudentMarks> getStudentMarksByCourseId(Long courseId);

    @Query("select sm from StudentMarks sm where sm.reportCardDesign.id = ?1 and sm.course.id = ?2")
    List<StudentMarks> getStudentMarksByRcdIdAndCourseId(Long rcdId, Long courseId);

    @Query("select sm from StudentMarks sm where sm.reportCardDesign.id = ?1 and sm.student.id = ?2")
    List<StudentMarks> getStudentMarksByRcdIdAndStudentId(Long rcdId, Long studentId);

    @Query("select sm from StudentMarks sm where sm.course.id = ?1 and sm.reportCardDesign.id = ?2 " +
        "and sm.student.id in (select student.id from StudentStandard where active is true and standard.id = ?3)")
    List<StudentMarks> getStudentMarksByCourseIdAndRcdIdAndStandardId(Long courseId, Long rcdId, Long standardId);

    @Query("select sm from StudentMarks sm where sm.course.id = ?1 and sm.reportCardDesign.exam.id = ?2 " +
        "and sm.student.id in (select student.id from StudentStandard where active is true and standard.id = ?3)")
    List<StudentMarks> getStudentMarksByCourseIdAndExamIdAndStandardId(Long courseId, Long examId, Long standardId);

    @Query("select sm from StudentMarks sm where sm.reportCardDesign.exam.id = ?1  " +
        "and sm.student.id in (select student.id from StudentStandard where active is true and standard.id = ?2)")
    List<StudentMarks> getStudentMarksByExamIdAndStandardId(Long examId, Long standardId);

    @Query("select sm from StudentMarks sm where sm.course.id = ?1 " +
        "and sm.student.id in (select student.id from StudentStandard where active is true and standard.id = ?2)")
    List<StudentMarks> getStudentMarksByCourseIdAndStandardId(Long courseId, Long standardId);

    @Query("select sm from StudentMarks sm where sm.student.id=?1 and  (sm.course.id=?2 and sm.reportCardDesign.fieldType = 'MAIN' and sm.reportCardDesign.exam.startDate between ?3 and ?4)")
    List<StudentMarks> getByCourseIdAndStudentId(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate);

    @Query("select sm from StudentMarks sm where sm.student.id=?1 and (sm.event.id in ?2)")
    List<StudentMarks> getByStudentIdAndEventIds(Long studentId, List<Long> eventIds);

    @Query("select sm.marks from StudentMarks sm where sm.reportCardDesign.id = ?1 and sm.course.id = ?2 and sm.student.id = ?3")
    Double getStudentMarksByRcdIdAndCourseIdAndStudentId(Long rcdId, Long courseId, Long studentId);

    @Modifying
    @Query("delete from StudentMarks sm where sm.reportCardDesign.id in ?1")
    void deleteStudentMarksByRcdIds(List<Long> rcdId);

    @Modifying
    @Query("delete from StudentMarks sm where sm.id in ?1")
    void deleteStudentMarksByIds(List<Long> ids);

    @Query("select avg(marks) from StudentMarks sm where sm.course.id = ?1 and sm.reportCardDesign.id = ?2 and sm.reportCardDesign.grade = ?3")
    Double getAvg(Long courseId, Long reportCardDesignId, Grade grade);

    @Query("select count(sm) from StudentMarks sm where sm.course.id = ?1 and sm.reportCardDesign.id = ?2 and sm.reportCardDesign.grade = ?3")
    Integer getCount(Long courseId, Long reportCardDesignId, Grade grade);

    @Query("select avg(marks) from StudentMarks sm where sm.course.id = ?1 and sm.reportCardDesign.id = ?2  and sm.student.id in (select ss.student.id from StudentStandard ss where ss.standard.id= ?3) ")
    Double getAvgOfStandard(Long courseId, Long reportCardDesignId, Long standardId);

    @Query("select count(sm) from StudentMarks sm where sm.course.id = ?1 and sm.reportCardDesign.id = ?2 and sm.student.id in (select ss.student.id from StudentStandard ss where ss.standard.id= ?3)")
    Integer getCountOfStandard(Long courseId, Long reportCardDesignId, Long standardId);

}




