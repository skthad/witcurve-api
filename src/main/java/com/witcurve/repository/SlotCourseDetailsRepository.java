package com.witcurve.repository;

import com.witcurve.domain.SlotCourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface SlotCourseDetailsRepository extends JpaRepository<SlotCourseDetails, Long> {

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.standard.id=?1 and scd.gsd.exam is null and scd.deleted=false and scd.gsd.status = 'ACTIVE' order by scd.dayOfWeek asc, scd.gsd.start asc")
    List<SlotCourseDetails> findByStandardIdOrderByGsdStartTime(Long standardId);

    @Query("Select scd from SlotCourseDetails scd where scd.courseTeacher.teacher.id=?1 and scd.gsd.exam is null and scd.deleted=false and scd.gsd.status = 'ACTIVE' order by scd.dayOfWeek asc, scd.gsd.start asc")
    List<SlotCourseDetails> findByTeacherIdOrderByGsdStartTime(Long teacherId);

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.id=?1 and scd.dayOfWeek = ?2 and scd.deleted=false")
    List<SlotCourseDetails> findActiveScdByGsdAndDayOfWeek(Long gsdId, DayOfWeek dayOfWeek);

    @Query("select distinct scd.courseTeacher.teacher.id from SlotCourseDetails scd where " +
        "scd.courseTeacher.active = true and scd.courseTeacher.course.omitSlotConflict=false and " +
        "scd.gsd.status = 'ACTIVE' and " +
        "((cast(scd.gsd.start as int) >= ?1 and cast(scd.gsd.start as int) < ?2) or " +
        "(cast(scd.gsd.start as int) + scd.gsd.duration > ?1 and " +
        "cast(scd.gsd.start as int) + scd.gsd.duration <= ?2)) and " +
        "scd.dayOfWeek = ?3 and scd.courseTeacher.teacher.schoolInfo.id = ?4")
    List<Long> findAllocatedTeachersList(Integer startTime, Integer endTime, DayOfWeek dayOfWeek, Long schoolInfoId);

    @Query("select distinct sub.teacher.id from Substitution sub where " +
        "sub.date = ?1 and sub.scd.courseTeacher.teacher.schoolInfo.id = ?2 and " +
        "((cast(sub.scd.gsd.start as int) >= ?1 and cast(sub.scd.gsd.start as int) < ?2) or " +
        "(cast(sub.scd.gsd.start as int) + sub.scd.gsd.duration > ?1 and " +
        "cast(sub.scd.gsd.start as int) + sub.scd.gsd.duration <= ?2))")
    List<Long> findSubstitutedTeacherList(LocalDate date, Long schoolInfoId, Integer startTime, Integer endTime);

    @Query("select scd from SlotCourseDetails scd where scd.courseTeacher.id=?1 and scd.gsd.status = 'ACTIVE'")
    List<SlotCourseDetails> findByCourseTeacherId(Long courseTeacherId);
}
