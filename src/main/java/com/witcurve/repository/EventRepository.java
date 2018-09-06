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

    @Query("Select e from Event e where e.eventDate = ?1 and" +
        "(" +
        "(e.student.id = ?2 and e.academicSession.id=?5 and e.type = 'LEAVE') or " +
        "(e.standard.id=?3 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT')) or " +
        "(e.grade=?4 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT')) or" +
        "(e.academicSession.id=?5 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT' or e.type='LEAVE'))" +
        ")" +
        " order by e.scd.gsd.start asc")
    List<Event> findEventsByDate(LocalDate date, Long studentId, Long classId, Grade grade, Long sessionId);

    @Query("Select e from Event e where e.eventDate between ?1 and ?2 and (e.type = 'LEAVE' or e.type = 'EXAM' or e.type = 'HOLIDAY' or e.type = 'SCHOOL_EVENT') and e.academicSession.id=?3 order by e.eventDate asc")
    List<Event> findEventsDuringMonth(LocalDate monthStart, LocalDate monthEnd, Long sessionId);

    @Query("Select e from Event e where e.eventDate between ?1 and ?2 and" +
        "(" +
        "(e.student.id = ?3 and e.academicSession.id=?6 and e.type = 'LEAVE') or " +
        "(e.standard.id=?4 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT')) or " +
        "(e.grade=?5 and (e.type = 'DAILY_UPDATE' or e.type = 'TEST' or e.type = 'ASSIGNMENT')) or" +
        "(e.academicSession.id=?6 and (e.type='HOLIDAY' or e.type='SCHOOL_EVENT' or e.type='LEAVE'))" +
        ")" +
        " order by e.scd.gsd.start asc")
    List<Event> findEventsDuringWeek(LocalDate monthStart, LocalDate monthEnd,
                                     Long studentId, Long classId, Grade grade,
                                     Long sessionId);
}

