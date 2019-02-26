package com.witcurve.repository;

import com.witcurve.domain.SlotCourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;

public interface SlotCourseDetailsRepository extends JpaRepository<SlotCourseDetails, Long> {

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.standard.id=?1 and scd.gsd.exam is null and scd.gsd.status = 'ACTIVE' order by scd.dayOfWeek asc, scd.gsd.start asc")
    List<SlotCourseDetails> findByStandardIdOrderByGsdStartTime(Long standardId);

    @Query("Select scd from SlotCourseDetails scd where scd.courseTeacher.teacher.id=?1 and scd.gsd.exam is null and scd.gsd.status = 'ACTIVE' order by scd.dayOfWeek asc, scd.gsd.start asc")
    List<SlotCourseDetails> findByTeacherIdOrderByGsdStartTime(Long teacherId);

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.id=?1 and scd.dayOfWeek = ?2")
    SlotCourseDetails findByGsdAndDayOfWeek(Long gsdId, DayOfWeek dayOfWeek);

    @Query("select scd from SlotCourseDetails scd where scd.courseTeacher.teacher.id in ?1 and scd.dayOfWeek = ?2")
    List<SlotCourseDetails> allocatedCourses(List<Long> teacherIds, DayOfWeek dayOfWeek);

    @Query("select distinct scd.courseTeacher.teacher.id from SlotCourseDetails scd where " +
        "scd.courseTeacher.active = true and " +
        "scd.gsd.status = 'ACTIVE' and " +
        "((cast(scd.gsd.start as int) >= ?1 and cast(scd.gsd.start as int) < ?2) or " +
        "(cast(scd.gsd.start as int) + scd.gsd.duration > ?1 and " +
        "cast(scd.gsd.start as int) + scd.gsd.duration <= ?2)) and " +
        "scd.dayOfWeek = ?3 and scd.courseTeacher.teacher.schoolInfo.id = ?4")
    List<Long> findAllocatedTeachersList(Integer startTime, Integer endTime, DayOfWeek dayOfWeek, Long schoolInfoId);

    @Query("select scd from SlotCourseDetails scd where scd.courseTeacher.id=?1 and scd.gsd.status = 'ACTIVE'")
    List<SlotCourseDetails> findByCourseTeacherId(Long courseTeacherId);
}
