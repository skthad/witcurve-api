package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.domain.StudentClass;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.EventRepository;
import com.witcurve.repository.StudentClassRepository;
import com.witcurve.service.EventService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log  = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;

    @Autowired
    StudentClassRepository studentClassRepository;


    @Override
    public EventDTO saveOrUpdate(EventDTO eventDTO) throws WitcurveException {
        log.debug("Request to save or update eventDTO : {}", eventDTO);
        Event event = eventMapper.toEntity(eventDTO);
        event = eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    @Override
    public EventDTO getEventById(Long eventId) throws WitcurveException {
        log.debug("Request to get event with id : {}", eventId);
        Event event = eventRepository.findById(eventId).get();

        if (event ==  null) {
            throw new WitcurveException("No Event with given id");
        }
        return eventMapper.toDto(event);
    }

    @Override
    public void deleteEvent(Long eventId) throws WitcurveException {
        log.debug("Request to delete event with id : {}", eventId);
        Event event = eventRepository.findById(eventId).get();

        if (event == null){
            throw new WitcurveException("No Event with given id");
        }
        eventRepository.delete(event);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenDateForStudent(LocalDate eventDate, Long studentId) {
        log.debug("Request to get tests with eventDate : {} for student with id : {}", eventDate, studentId);
        // need to get Events of type DailyUpdate, Test, Assignment, Holiday, Leave, SchoolEvent
        // null checks
        StudentClass studentClass = studentClassRepository.findByStudentId(studentId);
        Long classId = studentClass.getStandard().getId();
        Grade grade = studentClass.getStandard().getGrade();
        Long sessionId = studentClass.getStandard().getTerm().getSession().getId();

        List<Event> events = eventRepository.findEventsByDate(eventDate, studentId, classId, grade, sessionId);

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) {
        log.debug("Request to get tests with month no. : {} of year : {} or student with id : {}", month, year, studentId);
        // need to get Events of type Holiday, Leave, Exam, SchoolEvent
        // null checks
        StudentClass studentClass = studentClassRepository.findByStudentId(studentId);
        Long sessionId = studentClass.getStandard().getTerm().getSession().getId();
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).withDayOfMonth(month).minusDays(1);
        List<Event> events = eventRepository.findEventsDuringMonth(monthStart, monthEnd, sessionId);
        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenWeekForStudent(LocalDate weekDate, Integer year, Long studentId) {
        log.debug("Request to get tests with week having weekDate : {} of year : {} or student with id : {}", weekDate, year, studentId);

        // Go backward to get Monday
        LocalDate monday = weekDate;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY)
        {
            monday = monday.minusDays(1);
        }

        // Go forward to get Sunday
        LocalDate sunday = weekDate;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
        {
            sunday = sunday.plusDays(1);
        }
        StudentClass studentClass = studentClassRepository.findByStudentId(studentId);
        Long classId = studentClass.getStandard().getId();
        Grade grade = studentClass.getStandard().getGrade();
        Long sessionId = studentClass.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsDuringWeek(monday, sunday, studentId, classId, grade, sessionId);
        return eventMapper.toDto(events);

    }
}
