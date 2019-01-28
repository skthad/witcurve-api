package com.witcurve.repository;

import com.witcurve.domain.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {

    @Query("select cc from CourseContent cc where cc.course.id = ?1 order by cc.contentOrder")
    List<CourseContent> findByCourseId(Long courseId);

    @Query("select cc from CourseContent cc where cc.course.id = ?1 and cc.parentContent is not null")
    List<CourseContent> findAllSubTopicsInCourse(Long courseId);

    @Query("select cc from CourseContent cc where cc.course.id = ?1")
    List<CourseContent> findAllTopicsInCourse(Long courseId);
}
