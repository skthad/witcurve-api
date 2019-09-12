package com.witcurve.repository;

import com.witcurve.domain.Course;
import com.witcurve.domain.UserMobileEndPoint;
import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select course from Course course where course.schoolInfo.id = ?1 and course.active=true and course.elective = false order by course.grade")
    List<Course> findNonElectivesBySchoolInfo(Long schoolInfoId);

    @Query("select course from Course course where course.schoolInfo.id = ?1 and course.active=true and course.elective = false order by course.grade")
    List<Course> findNonElectivesByStandardId(Long standardId);

    @Query("select course from Course course where course.schoolInfo.id = ?1 and course.grade = ?2 and course.active=true order by  course.courseType desc, course.displayName asc")
    List<Course> findBySchoolInfoAndGrade(Long schoolInfoId, Grade grade);

    @Query("select course from Course course where course.schoolInfo.id = ?1 and course.grade = ?2 and course.courseCode = ?3")
    List<Course> findBySchoolInfoAndGradeAndCourseCode(Long schoolInfoId, Grade grade, String courseCode);

    @Query("Select scd.courseTeacher.course from SlotCourseDetails scd where  scd.id=?1")
    Course findBySlotCourseDetailId(Long scdId);

    @Query("Select ct.course from CourseTeacher ct where ct.id = ?1")
    Course findByCourseTeacherId(Long courseTeacherId);

    @Query("Select course from Course course where course.schoolInfo.id = ?1 and course.grade = ?2 and course.courseType = ?3 and course.active=true order by  course.courseType desc, course.displayName asc")
    List<Course> findBySchoolInfoAndGradeAndCourseType(Long schoolInfo, Grade grade, CourseType courseType);

    @Query("Select course from Course course where course.schoolInfo.id = ?1 and course.grade = ?2 and course.courseType = ?3 and course.elective=true and course.active=true order by  course.courseType desc, course.displayName asc")
    List<Course>findElectiveCourse(Long schoolInfo, Grade grade, CourseType courseType);

    @Query("Select course from Course course where course.schoolInfo.id = ?1 and course.grade = ?2 and course.courseType = ?3 and course.mandatory=true and course.active=true order by  course.courseType desc, course.displayName asc" )
    List<Course>findMandatoryCourse(Long schoolInfo, Grade grade, CourseType courseType);


}
