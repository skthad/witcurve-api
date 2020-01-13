package com.witcurve.repository;

import com.witcurve.domain.CourseGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourseGradeRepository extends JpaRepository<CourseGrade, Long> {

    @Query("select cg from CourseGrade cg where cg.reportCardDesign.exam.id = ?1")
    List<CourseGrade> getCourseGradeByExamId(Long examId);

    @Query("select cg from CourseGrade cg where cg.course.id = ?1")
    List<CourseGrade> getCourseGradeByCourseId(Long courseId);

    @Query("select cg from CourseGrade cg where cg.course.id = ?1 and cg.reportCardDesign.id = ?2 " +
        "and cg.student.id in (select student.id from StudentStandard where active is true and standard.id = ?3)")
    List<CourseGrade> getCourseGradeByCourseIdAndRcdIdAndStandardId(Long courseId, Long rcdId, Long standardId);

    @Query("select cg from CourseGrade cg where cg.course.id = ?1 and cg.reportCardDesign.exam.id = ?2 " +
        "and cg.student.id in (select student.id from StudentStandard where active is true and standard.id = ?3)")
    List<CourseGrade> getCourseGradeByCourseIdAndExamIdAndStandardId(Long courseId, Long examId, Long standardId);

    @Query("select cg from CourseGrade cg where cg.reportCardDesign.exam.id = ?1  " +
        "and cg.student.id in (select student.id from StudentStandard where active is true and standard.id = ?2)")
    List<CourseGrade> getCourseGradeByExamIdAndStandardId(Long examId, Long standardId);

    @Query("select cg from CourseGrade cg where cg.student.id=?1 and  (cg.course.id=?2 and cg.reportCardDesign.fieldType ='NON_SCHOLASTIC' and cg.reportCardDesign.exam.startDate between ?3 and ?4)")
    List<CourseGrade> getByCourseIdAndStudentId(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("delete from CourseGrade cg where cg.id in ?1")
    void deleteStudentGradesByIds(List<Long> ids);

    @Query("select cg from CourseGrade cg where cg.reportCardDesign.id = ?1 and cg.course.id = ?2")
    List<CourseGrade> getStudentGradesByRcdIdAndCourseId(Long rcdId, Long courseId);

    @Query("select cg from CourseGrade cg where cg.reportCardDesign.id = ?1 and cg.student.id = ?2")
    List<CourseGrade> getCourseGradeByRcdIdAndStudentId(Long rcdId, Long studentId);
}
