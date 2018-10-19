package com.witcurve.repository;

import com.witcurve.domain.Event;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository  extends JpaRepository<Event, Long> {

    @Query("Select e from Event e where e.date = ?1 and" +
        "(" +
        "(e.student.id = ?2 and e.type = 'LEAVE') or " +
        "(e.standard.id=?3 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT'  or e.type='SCHOOL_EVENT')) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?4)) and e.academicSession.id=?5 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsByDateForStudent(LocalDate date, Long studentId, Long standardId, Grade grade, Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and " +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type='SCHOOL_EVENT' or e.type='EXAM')) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsDuringMonthForStudent(LocalDate monthStart, LocalDate monthEnd, Long studentId, Long standardId, Grade grade, Long sessionId);


    @Query("Select e.date from Event e where e.date between ?1 and ?2 and " +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type='SCHOOL_EVENT' or e.type='EXAM')) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<LocalDate> findEventDatesDuringMonthForStudent(LocalDate monthStart, LocalDate monthEnd, Long studentId, Long standardId, Grade grade, Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type = 'TEST' or e.type = 'ASSIGNMENT'  or e.type='SCHOOL_EVENT')) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsDuringWeekForStudent(LocalDate weekStart, LocalDate weekEnd,
                                     Long studentId, Long standardId, Grade grade,
                                     Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT'  or e.type='SCHOOL_EVENT'  or e.type='EXAM')) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsForDiaryForStudent(LocalDate startDate, LocalDate endDate,
                                                Long studentId, Long standardId, Grade grade,
                                                Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type = 'TEST' or e.type = 'ASSIGNMENT' or e.type='SCHOOL_EVENT' or e.type='EXAM')) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?5)) and e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsForAnnouncementsForStudent(LocalDate startDate, LocalDate endDate,
                                             Long studentId, Long standardId, Grade grade,
                                             Long sessionId);



    /*@Query("Select e from Event e where e.date = ?1 and" +
        "(" +
        "(e.standard.id=?2 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT'  or e.type='SCHOOL_EVENT')) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?3)) and e.academicSession.id=?4 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")")
    List<Event> findEventsByDateForStandard(LocalDate date, Long standardId, Grade grade, Long sessionId);*/

    @Query("Select e from Event e where e.date = ?1 and e.type = ?2 and e.scd.id = ?3")
    Event findEventOnDateAndSlot(LocalDate date, EventType type, Long scdId);

    @Query("Select e from Event e where e.date = ?1 and (e.academicSession.id=?3 or e.standard.term.session.id =?3) and e.type in ?2")
    List<Event> eventsBlockingHolidayAndSchoolEvents(LocalDate date, List<EventType> types, Long sessionId);

    @Query("Select e from Event e where e.date = ?1 and (e.academicSession.id=?3 or e.standard.term.session.id =?3 or e.student.id=?4) and e.type in ?2")
    List<Event> eventsBlockingLeave(LocalDate date, List<EventType> types, Long sessionId, Long studentId);

    @Query("Select e from Event e where e.bindingId = ?1 and " +
        "(" +
        "(e.student.id = ?2) or " +
        "(e.standard.id=?3) or " +
        "((e.grade is null or (e.grade is not null and e.grade = ?4)) and e.academicSession.id=?5)" +
        ")")
    List<Event> findEventsByBindingId(String bindingId, Long studentId, Long standardId, Grade grade,
                                      Long sessionId);

    @Query("Select e from Event e where e.student.id = ?1 and e.type = 'LEAVE' " +
        "and e.academicSession.id=?2 ")
    List<Event> findLeavesForStudent(Long studentId, Long sessionId);

    @Query("Select e from Event e where e.standard.id = ?1 and e.type = 'LEAVE' " +
        "and e.academicSession.id=?2 ")
    List<Event> findLeavesForStandard(Long studentId, Long sessionId);

    @Query("Select e from Event e where e.date = ?1 and e.student.id = ?2 " +
        "and e.type = 'LEAVE' and e.academicSession.id=?3")
    List<Event> findAttendanceForStudent(LocalDate date, Long studentId, Long sessionId);

    @Query("Select e from Event e where e.date = ?1 and e.standard.id = ?2 " +
        "and e.type = 'LEAVE' and e.academicSession.id=?3")
    List<Event> findAttendanceForStandard(LocalDate date, Long studentId, Long sessionId);
}

