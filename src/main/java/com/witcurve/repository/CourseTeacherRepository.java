package com.witcurve.repository;

import com.witcurve.domain.CourseTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseTeacherRepository extends JpaRepository<CourseTeacher, Long> {

    List<CourseTeacher> findByTeacherId(Long teacherId);
}
