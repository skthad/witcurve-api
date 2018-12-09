package com.witcurve.repository;

import com.witcurve.domain.Event;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface EventRepository  extends JpaRepository<Event, Long> {

    @Query("Select e from Event e where e.date = ?1 and" +
        "(" +
        "(e.student.id = ?2 and e.type = 'ATTENDANCE') or " +
        "(e.standard.id=?3 and e.type in ?6) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?4)) and e.academicSession.id=?5 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsByDateForStudent(LocalDate date, Long studentId, Long standardId, Grade grade, Long sessionId, List<EventType> types);

    @Query("Select e from Event e where e.date = ?1 and" +
        "(" +
        "(e.staff.id = ?2 and e.type = 'ATTENDANCE') or " +
        "(e.scd.courseTeacher.teacher.id=?2 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT')) or " +
        "(e.standard.id in ?3 and e.type='SCHOOL_EVENT') or " +
        "((e.grade is null or (e.grade is not null and e.grade in ?4)) and e.academicSession.id=?5 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsByDateForStaff(LocalDate date, Long staffId, Set<Long> standardIds, Set<Grade> grades, Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'ATTENDANCE') or " +
        "(e.standard.id=?4 and e.type in ?7) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsByDateRangeForStudent(LocalDate startDate, LocalDate endDate,
                                                Long studentId, Long standardId, Grade grade,
                                                Long sessionId, List<EventType> types);

    @Query("Select distinct e.date from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'ATTENDANCE') or " +
        "(e.standard.id=?4 and e.type in ?7) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<LocalDate> findEventDatesByDateRangeForStudent(LocalDate startDate, LocalDate endDate,
                                                Long studentId, Long standardId, Grade grade,
                                                Long sessionId, List<EventType> types);

    @Query("Select e from Event e where e.date = ?1 and e.type = ?2 and e.scd.id = ?3")
    Event findEventOnDateAndSlot(LocalDate date, EventType type, Long scdId);

    @Query("Select e from Event e where e.date = ?1 and (e.academicSession.id=?3 or e.standard.term.session.id =?3) and e.type in ?2")
    List<Event> eventsBlockingHolidayAndSchoolEvents(LocalDate date, List<EventType> types, Long sessionId);

    @Query("Select e from Event e where e.date = ?1 and (e.academicSession.id=?3 or e.standard.term.session.id =?3) and e.student.id=?4 and e.type in ?2")
    List<Event> eventsBlockingLeave(LocalDate date, List<EventType> types, Long sessionId, Long studentId);

    @Query("Select e from Event e where e.bindingId = ?1 and " +
        "(" +
        "(e.student.id = ?2) or " +
        "(e.standard.id=?3) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?4)) and e.academicSession.id=?5)" +
        ")")
    List<Event> findEventsByBindingId(String bindingId, Long studentId, Long standardId, Grade grade,
                                      Long sessionId);

    @Query("Select e from Event e where e.student.id = ?1 and e.type = 'ATTENDANCE' " +
        "and e.academicSession.id=?2 ")
    List<Event> findAllLeavesForStudentInSession(Long studentId, Long sessionId);

    @Query("Select e from Event e where e.standard.id = ?1 and e.type = 'ATTENDANCE' " +
        "and e.academicSession.id=?2 ")
    List<Event> findLeavesForStandard(Long studentId, Long sessionId);

    @Query("Select e from Event e where e.date = ?1 and e.student.id = ?2 " +
        "and e.type = 'ATTENDANCE'")
    List<Event> findLeaveForStudent(LocalDate date, Long studentId);

    @Query("Select e from Event e where e.date = ?1 and e.standard.id = ?2 " +
        "and e.type = 'ATTENDANCE'")
    List<Event> findAttendanceForStandard(LocalDate date, Long standardId);

    @Query("Select e from Event e where e.date = ?1 and e.student.id = ?2 " +
        "and e.type = 'ATTENDANCE'")
    List<Event> findAttendanceForStudent(LocalDate date, Long student);

    @Query("select distinct e.staff.id from Event e where e.type = 'ATTENDANCE' and " +
        "e.staff is not null and e.staff.id in ?1 and e.date = ?2")
    List<Long> findAbsentTeacherList(List<Long> teacherIds, LocalDate date);

    @Query("Select e from Event e where " +
        "(e.type ='NOTICE' or e.type ='STAFF_NOTICE') and (e.standard.term.session.id=?1 or e.academicSession.id=?1) " +
        "order by e.date desc")
    Page<Event> findAdminNotices(Long sessionId, Pageable pageable);

    @Query("Select e from Event e where e.type = 'NOTICE' and " +
        "e.standard.id =?1 or " +
        "(e.grade is null and e.academicSession.id=?3) or (e.grade = ?2 and e.academicSession.id=?3)  " +
        "order by e.date desc")
    Page<Event> findStudentNotices(Long standardId, Grade grade, Long sessionId, Pageable pageable);

    @Query("Select e from Event e where e.type = 'NOTICE' or e.type = 'STAFF_NOTICE' and " +
        "e.standard.id =?1 or " +
        "(e.grade is null and e.academicSession.id=?3) or (e.grade = ?2 and e.academicSession.id=?3)  " +
        "order by e.date desc")
    Page<Event> findClassTeacherNotices(Long standardId, Grade grade, Long sessionId, Pageable pageable);

    @Query("Select e from Event e where e.type = 'NOTICE' or e.type = 'STAFF_NOTICE' and " +
        "e.grade is null and e.academicSession.id=?1 " +
        "order by e.date desc")
    Page<Event> findTeacherNotices(Long sessionId, Pageable pageable);

}

