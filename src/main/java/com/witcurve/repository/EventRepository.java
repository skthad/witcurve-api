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
        "(e.grade=?4 and ( e.type = 'HOLIDAY' or e.type='SCHOOL_EVENT')) or" +
        "(e.academicSession.id=?5 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")" +
        " order by e.scd.gsd.start asc")
    List<Event> findEventsByDateForStudent(LocalDate date, Long studentId, Long standardId, Grade grade, Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and " +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and e.type='SCHOOL_EVENT') or " +
        "(e.grade=?5 and (e.type = 'SCHOOL_EVENT' or e.type = 'HOLIDAY')) or" +
        "(e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")" +
        " order by e.date asc")
        List<Event> findEventsDuringMonthForStudent(LocalDate monthStart, LocalDate monthEnd, Long studentId, Long standardId, Grade grade, Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type = 'TEST' or e.type = 'ASSIGNMENT'  or e.type='SCHOOL_EVENT')) or " +
        "(e.grade=?5 and ( e.type = 'HOLIDAY' or e.type='SCHOOL_EVENT')) or" +
        "(e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")" +
        " order by e.date asc, e.scd.gsd.start asc")
    List<Event> findEventsDuringWeekForStudent(LocalDate weekStart, LocalDate weekEnd,
                                     Long studentId, Long standardId, Grade grade,
                                     Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT'  or e.type='SCHOOL_EVENT'  or e.type='EXAM')) or " +
        "(e.grade=?5 and ( e.type = 'HOLIDAY' or e.type='SCHOOL_EVENT')) or" +
        "(e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")" +
        " order by e.date desc, e.scd.gsd.start asc")
    List<Event> findEventsForDiaryForStudent(LocalDate startDate, LocalDate endDate,
                                                Long studentId, Long classId, Grade grade,
                                                Long sessionId);

    @Query("Select e from Event e where e.date between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type = 'TEST' or e.type = 'ASSIGNMENT' or e.type='SCHOOL_EVENT' or e.type='EXAM')) or " +
        "(e.grade=?5 and ( e.type = 'HOLIDAY' or e.type='SCHOOL_EVENT')) or" +
        "(e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")" +
        " order by e.date asc, e.scd.gsd.start asc")
    List<Event> findEventsForAnnouncementsForStudent(LocalDate startDate, LocalDate endDate,
                                             Long studentId, Long classId, Grade grade,
                                             Long sessionId);



    @Query("Select e from Event e where e.date = ?1 and" +
        "(" +
        "(e.standard.id=?2 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT'  or e.type='SCHOOL_EVENT')) or " +
        "(e.grade=?3 and ( e.type = 'HOLIDAY' or e.type='SCHOOL_EVENT')) or" +
        "(e.academicSession.id=?4 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT'))" +
        ")" +
        " order by e.scd.gsd.start asc")
    List<Event> findEventsByDateForStandard(LocalDate date, Long standardId, Grade grade, Long sessionId);

    @Query("Select e from Event e where e.date = ?1 and e.type = ?2 and e.scd.id = ?3")
    Event findEventOnDateAndSlot(LocalDate date, EventType type, Long scdId);

    @Query
    Event findEventByDateAndType(LocalDate date, EventType type);

    List<Event> findEventsByBindingId(String bindingId);
}

