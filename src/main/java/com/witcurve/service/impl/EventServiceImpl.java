package com.witcurve.service.impl;

import com.witcurve.domain.Class;
import com.witcurve.domain.Event;
import com.witcurve.domain.StudentClass;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.ClassRepository;
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

    @Autowired
    ClassRepository classRepository;


    @Override
    public List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs) throws WitcurveException {
        log.debug("Request to save or update eventDTOs : {}", eventDTOs);
        for(EventDTO eventDTO : eventDTOs) {
            if(eventDTO.getType().equals(EventType.DAILY_UPDATE) || eventDTO.getType().equals(EventType.TEST) || eventDTO.getType().equals(EventType.ASSIGNMENT)) {
                Event event = eventRepository.findEventOnDateAndSlot(eventDTO.getDate(), eventDTO.getType(), eventDTO.getScd().getId());
                if(event == null) {
                    throw new WitcurveException("There already exists a record for given event type : "+eventDTO.getType()+" for scd with id : "+eventDTO.getScd().getId()+ " on date : "+eventDTO.getDate().toString());
                }
            }
        }
        List<Event> events = eventMapper.toEntity(eventDTOs);
        events = eventRepository.saveAll(events);
        return eventMapper.toDto(events);
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
    public List<EventDTO> findAllEventsOnGivenDateForClass(LocalDate eventDate, Long classId) throws WitcurveException {
        log.debug("Request to get tests with eventDate : {} for class with id : {}", eventDate, classId);

        Class standard  = classRepository.findById(classId).get();
        if(standard == null) {
            throw new WitcurveException("There is no class with given class id : "+classId);
        }
        Grade grade = standard.getGrade();
        Long sessionId = standard.getTerm().getSession().getId();

        List<Event> events = eventRepository.findEventsByDateForClass(eventDate, classId, grade, sessionId);

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenDateForStudent(LocalDate eventDate, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with eventDate : {} for student with id : {}", eventDate, studentId);
        // null checks
        StudentClass studentClass = studentClassRepository.findByStudentId(studentId);
        if(studentClass == null) {
            throw new WitcurveException("There is no student class with given student id : "+studentId);
        }
        Long classId = studentClass.getStandard().getId();
        Grade grade = studentClass.getStandard().getGrade();
        Long sessionId = studentClass.getStandard().getTerm().getSession().getId();

        List<Event> events = eventRepository.findEventsByDateForStudent(eventDate, studentId, classId, grade, sessionId);

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with month no. : {} of year : {} or student with id : {}", month, year, studentId);
        // need to get Events of type Holiday, Leave, Exam, SchoolEvent
        // null checks
        StudentClass studentClass = studentClassRepository.findByStudentId(studentId);
        if(studentClass == null) {
            throw new WitcurveException("There is no student class with given student id : "+studentId);
        }
        Long classId = studentClass.getStandard().getId();
        Grade grade = studentClass.getStandard().getGrade();
        Long sessionId = studentClass.getStandard().getTerm().getSession().getId();
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).withDayOfMonth(month).minusDays(1);
        List<Event> events = eventRepository.findEventsDuringMonthForStudent(monthStart, monthEnd, studentId, classId, grade, sessionId);
        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenWeekForStudent(LocalDate weekDate, Integer year, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with week having weekDate : {} of year : {} or student with id : {}", weekDate, year, studentId);

        // Go backward to get Sunday
        LocalDate sunday = weekDate;
        while (sunday.getDayOfWeek() != DayOfWeek.SUNDAY)
        {
            sunday = sunday.minusDays(1);
        }

        // Go forward to get Saturday
        LocalDate saturday = weekDate;
        while (saturday.getDayOfWeek() != DayOfWeek.SATURDAY)
        {
            saturday = saturday.plusDays(1);
        }
        StudentClass studentClass = studentClassRepository.findByStudentId(studentId);
        Long classId = studentClass.getStandard().getId();
        Grade grade = studentClass.getStandard().getGrade();
        Long sessionId = studentClass.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsDuringWeekForStudent(sunday, saturday, studentId, classId, grade, sessionId);
        return eventMapper.toDto(events);

    }
}
