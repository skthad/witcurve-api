package com.witcurve.repository;

import com.witcurve.domain.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {

    @Query("select cc from CourseContent cc where cc.course.id = ?1")
    List<CourseContent> findByCourseId(Long courseId);

    @Query("delete from CourseContent where course.id = ?1 and parentContent is not null")
    void deleteAllSubTopics(Long courseId);

    @Query("delete from CourseContent where course.id = ?1")
    void deleteAllTopics(Long courseId);
}
