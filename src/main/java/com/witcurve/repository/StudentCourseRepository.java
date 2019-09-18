package com.witcurve.repository;

import com.witcurve.domain.Course;
import com.witcurve.domain.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {


    @Modifying
    @Query("delete from StudentCourse where studentStandard.id = ?1 and course.id = ?2")
    void unmapStudentCourse(Long studentStandardId, Long courseId);

    @Query("Select sc from StudentCourse sc where sc.studentStandard.id=?1 and sc.course.id=?2")
    StudentCourse getStudentCourseByStudentStandardIdAndCourseId(Long studentStandardId, Long courseId);


    @Query("Select sc from StudentCourse sc where sc.studentStandard.student.id = ?1 and sc.active=true and ((sc.course.courseType = 'SCHOLASTIC'" +
        " and sc.course.elective=true) or sc.course.courseType= 'NON_SCHOLASTIC') order by sc.course.courseType desc, sc.course. displayName asc")
    List<StudentCourse> getStudentCoursesByStudentId(Long studentId);

    @Query("Select sc from StudentCourse sc where sc.studentStandard.standard.id = ?1 and sc.active=true and ((sc.course.courseType = 'SCHOLASTIC'" +
        " and sc.course.elective=true) or sc.course.courseType= 'NON_SCHOLASTIC') order by sc.studentStandard.rollNo asc, sc.course.courseType desc," +
        " sc.course. displayName asc")
    List<StudentCourse> getStudentCoursesByStandardId(Long standardId);

    @Modifying
    @Query("update StudentCourse sc set sc.active=false where sc.id in ?1")
    void deactivateStudentCourseByIds(List<Long> ids);

    @Modifying
    @Query("update StudentCourse sc set sc.active=false where sc.studentStandard.id in (Select ss.id from StudentStandard ss where ss.student.id in ?1)")
    void deactivateStudentCourseByStudentIds(List<Long> studentIds);

    @Query("Select sc.id from StudentCourse sc where sc.course.id=?1 and sc.active=true")
    List<Long> getByCourseId(Long courseId);

    @Query("Select sc.course from StudentCourse sc where sc.active=true and sc.studentStandard.student.id = ?1")
    List<Course> getByStudentId(Long studentId);

    @Query("Select sc.course.id from StudentCourse sc where sc.active=true and sc.studentStandard.student.id = ?1")
    List<Long> getCourseIdsByStudentId(Long studentId);

}
