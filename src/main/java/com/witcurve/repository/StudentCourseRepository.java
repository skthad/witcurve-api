package com.witcurve.repository;

import com.witcurve.domain.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    @Query("select sc.course.id from StudentCourse sc where sc.studentStandard.id = ?1")
    List<Long> findCourseIdsByStudentStandard(Long studentStandardId);

    @Modifying
    @Query("delete from StudentCourse where studentStandard.id = ?1 and course.id = ?2")
    void unmapStudentCourse(Long studentStandardId, Long courseId);
}
