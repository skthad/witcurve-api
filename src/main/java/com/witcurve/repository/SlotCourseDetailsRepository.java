package com.witcurve.repository;

import com.witcurve.domain.SlotCourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.util.List;

public interface SlotCourseDetailsRepository extends JpaRepository<SlotCourseDetails, Long> {

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.standard.id=?1 and scd.gsd.exam is null order by scd.dayOfWeek asc, scd.gsd.start asc")
    List<SlotCourseDetails> findByStandardIdOrderByGsdStart(Long standardId);

    @Query("Select scd from SlotCourseDetails scd where scd.courseTeacher.teacher.id=?1 and scd.gsd.standard.term.id=?2 and scd.gsd.exam is null order by scd.dayOfWeek asc, scd.gsd.start asc")
    List<SlotCourseDetails> findByTeacherIdAndTermIdOrderByGsdStart(Long teacherId, Long termId);

    @Query("Select scd from SlotCourseDetails scd where scd.gsd.id=?1 and scd.dayOfWeek = ?2")
    SlotCourseDetails findByGsdAndDayOfWeek(Long gsdId, DayOfWeek dayOfWeek);

}
