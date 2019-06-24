package com.witcurve.repository;

import com.witcurve.domain.Course;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select course from Course course where course.schoolInfo.id = ?1 and course.grade = ?2 and course.active=true")
    List<Course> findBySchoolInfoAndGrade(Long schoolInfoId, Grade grade);

    @Query("select course from Course course where course.schoolInfo.id = ?1 and course.grade = ?2 and course.courseCode = ?3")
    List<Course> findBySchoolInfoAndGradeAndCourseCode(Long schoolInfoId, Grade grade, String courseCode);

}
