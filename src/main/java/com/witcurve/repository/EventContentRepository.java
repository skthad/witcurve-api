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

    @Query("select ec.courseContent from EventContent ec where ec.event.id = ?1 order by ec.courseContent.contentOrder")
    List<CourseContent> findCourseContentsByEventId(Long eventId);

    @Query("select ec.courseContent from EventContent ec where ec.ecd.id = ?1 order by ec.courseContent.contentOrder")
    List<CourseContent> findCourseContentsByEcdId(Long ecdId);

    @Modifying
    @Query("delete from EventContent ec where ec.event.id = ?1")
    void deleteByEventId(Long eventId);

    @Modifying
    @Query("delete from EventContent ec where ec.ecd.id = ?1")
    void deleteByEcdId(Long ecdId);

    @Modifying
    @Query("delete from EventContent ec where ec.courseContent.id in ?1")
    void deleteByCourseContentIds(List<Long> courseContentIds);

    @Modifying
    @Query("delete from EventContent ec where ec.courseContent.id = ?1")
    void deleteByCourseContentId(Long courseContentId);

}
