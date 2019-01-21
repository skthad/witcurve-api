package com.witcurve.repository;

import com.witcurve.domain.SlotCourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;

public interface SlotCourseDetailsRepository extends JpaRepository<SlotCourseDetails, Long> {

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.standard.id=?1 and scd.gsd.exam is null order by scd.dayOfWeek asc, scd.gsd.startTime asc")
    List<SlotCourseDetails> findByStandardIdOrderByGsdStartTime(Long standardId);

    @Query("Select scd from SlotCourseDetails scd where scd.courseTeacher.teacher.id=?1 and scd.gsd.standard.term.id=?2 and scd.gsd.exam is null order by scd.dayOfWeek asc, scd.gsd.startTime asc")
    List<SlotCourseDetails> findByTeacherIdAndTermIdOrderByGsdStartTime(Long teacherId, Long termId);

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.id=?1 and scd.dayOfWeek = ?2")
    SlotCourseDetails findByGsdAndDayOfWeek(Long gsdId, DayOfWeek dayOfWeek);

    @Query("select scd from SlotCourseDetails scd where scd.courseTeacher.teacher.id in ?1 and scd.dayOfWeek = ?2")
    List<SlotCourseDetails> allocatedCourses(List<Long> teacherIds, DayOfWeek dayOfWeek);

    @Query("select distinct scd.courseTeacher.teacher.id from SlotCourseDetails scd where scd.courseTeacher.teacher.id in ?1 " +
        "and scd.dayOfWeek = ?2 and ((scd.gsd.startTime >= ?3 and scd.gsd.startTime < ?4) " +
        "or (scd.gsd.startTime + scd.gsd.duration > ?3 and scd.gsd.startTime + scd.gsd.duration <= ?4))")
    List<Long> allocatedTeacherList(List<Long> availableStaff, DayOfWeek dayOfWeek, Integer start, Integer end);
}
