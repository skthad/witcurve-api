package com.witcurve.repository;

import com.witcurve.domain.CourseContent;
import com.witcurve.domain.EventContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventContentRepository extends JpaRepository<EventContent, Long> {

    @Query("select ec.courseContent from EventContent ec where ec.eventId = ?1 order by ec.courseContent.contentOrder")
    List<CourseContent> findCourseContentsByEventId(Long eventId);

    @Modifying
    @Query("delete from EventContent where eventId = ?1")
    void deleteByEventId(Long eventId);

}
