package com.witcurve.service.impl;

import com.witcurve.domain.Event;
import com.witcurve.domain.Standard;
import com.witcurve.domain.StudentStandard;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.repository.EventRepository;
import com.witcurve.repository.StandardRepository;
import com.witcurve.repository.StudentStandardRepository;
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
import java.util.*;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final Logger log  = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventMapper eventMapper;

    @Autowired
    StudentStandardRepository studentStandardRepository;

    @Autowired
    StandardRepository standardRepository;

    private static final ArrayList<EventType> firstList = new ArrayList<EventType>(
        Arrays.asList(EventType.DAILY_UPDATE, EventType.TEST, EventType.ASSIGNMENT, EventType.EXAM));


    @Override
    public List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs) throws WitcurveException {
        log.debug("Request to save or update eventDTOs : {}", eventDTOs);

        List<Event> eventList = new ArrayList<>();
        if (eventDTOs.size() > 1) {
            String bindingId = UUID.randomUUID().toString();
            for(EventDTO eventDTO : eventDTOs) {
                eventDTO.setBindingId(bindingId);
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
    public List<EventDTO> findAllEventsOnGivenDateForStandard(LocalDate eventDate, Long standardId) throws WitcurveException {
        log.debug("Request to get tests with eventDate : {} for standard with id : {}", eventDate, standardId);

        Standard standard  = standardRepository.findById(standardId).get();
        if(standard == null) {
            throw new WitcurveException("There is no standard with given standard id : "+standardId);
        }
        Grade grade = standard.getGrade();
        Long sessionId = standard.getTerm().getSession().getId();

        List<Event> events = eventRepository.findEventsByDateForStandard(eventDate, standardId, grade, sessionId);
        Collections.sort(events, new EventDateAscComparator());

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenDateForStudent(LocalDate eventDate, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with eventDate : {} for student with id : {}", eventDate, studentId);
        // null checks
        StudentStandard studentStandard = studentStandardRepository.findByStudentId(studentId);
        if(studentStandard == null) {
            throw new WitcurveException("There is no student standard with given student id : "+studentId);
        }
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();

        List<Event> events = eventRepository.findEventsByDateForStudent(eventDate, studentId, standardId, grade, sessionId);
        Collections.sort(events, new EventDateAscComparator());

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with month no. : {} of year : {} or student with id : {}", month, year, studentId);
        // need to get Events of type Holiday, Leave, Exam, SchoolEvent
        // null checks
        StudentStandard studentStandard = studentStandardRepository.findByStudentId(studentId);
        if(studentStandard == null) {
            throw new WitcurveException("There is no student standard with given student id : "+studentId);
        }
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).withDayOfMonth(month).minusDays(1);
        List<Event> events = eventRepository.findEventsDuringMonthForStudent(monthStart, monthEnd, studentId, standardId, grade, sessionId);
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenWeekForStudent(LocalDate weekDate, Integer year, Long studentId) throws WitcurveException{
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
        StudentStandard studentStandard = studentStandardRepository.findByStudentId(studentId);
        if(studentStandard == null) {
            throw new WitcurveException("There is no student standard with given student id : "+studentId);
        }
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsDuringWeekForStudent(sunday, saturday, studentId, standardId, grade, sessionId);
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> findAllEventsForDiary(LocalDate date, Long studentId) throws WitcurveException {
        log.debug("Find events for diary for a duration of week from date : {} and for student with id : {}", date, studentId);
        LocalDate startDate = date.minusDays(7);
        StudentStandard studentStandard = studentStandardRepository.findByStudentId(studentId);
        if(studentStandard == null) {
            throw new WitcurveException("There is no student standard with given student id : "+studentId);
        }
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsForDiaryForStudent(startDate, date, studentId, standardId, grade, sessionId);
        String lastBindingId = events.get(events.size()-1).getBindingId();
        if(lastBindingId != null) {
            List<Event> remainingList = eventRepository.findEventsByBindingId(lastBindingId);
            if(remainingList.size() != 0) {
                events.addAll(remainingList);
            }
        }
        Collections.sort(events, new EventDateDescComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> findAllEventsForAnnouncements(LocalDate date, Long studentId) throws WitcurveException {
        log.debug("Find events for announcements for a duration of week from date : {} and for student with id : {}", date, studentId);
        LocalDate endDate = date.plusDays(7);
        StudentStandard studentStandard = studentStandardRepository.findByStudentId(studentId);
        if(studentStandard == null) {
            throw new WitcurveException("There is no student standard with given student id : "+studentId);
        }
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsForAnnouncementsForStudent(date, endDate, studentId, standardId, grade, sessionId);
        String lastBindingId = events.get(events.size()-1).getBindingId();
        if(lastBindingId != null) {
            List<Event> remainingList = eventRepository.findEventsByBindingId(lastBindingId);
            if(remainingList.size() != 0) {
                events.addAll(remainingList);
            }
        }
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);

    }

    private void isEventValid(EventDTO eventDTO) throws WitcurveException {
        if(firstList.contains(eventDTO.getType())) {
            if(eventDTO.getType().equals(EventType.ASSIGNMENT)) {
                if(eventDTO.getStandardId() == null) {
                    throw new WitcurveException("Event of type : "+eventDTO.getType()+"cannot have empty standardId");
                }
            } else {
                if(eventDTO.getStandardId() == null || eventDTO.getScd() == null || (eventDTO.getScd() != null && eventDTO.getScd().getId() == null)) {
                    throw new WitcurveException("Event of type : "+eventDTO.getType()+"cannot have empty standardId and scd");
                }
            }
            Event event = eventRepository.findEventOnDateAndSlot(eventDTO.getDate(), eventDTO.getType(), eventDTO.getScd().getId());
            if(event != null) {
                throw new WitcurveException("There already exists a record for given event type : "+eventDTO.getType()+" for scd with id : "+eventDTO.getScd().getId()+ " on date : "+eventDTO.getDate().toString());
            }
        } else if(eventDTO.getType().equals(EventType.HOLIDAY)) {
            if(!(eventDTO.getGrade() == null ^ eventDTO.getAcademicSessionId() == null)) {
                throw new WitcurveException("Event of type : "+eventDTO.getType()+"should have only of the fields : grade, academicSessionId");
            }
            // add a check for event existence - check for all types
        } else if(eventDTO.getType().equals(EventType.SCHOOL_EVENT)) {
            if(!(eventDTO.getGrade() == null ^ eventDTO.getAcademicSessionId() == null ^ eventDTO.getStandardId() == null)) {
                throw new WitcurveException("Event of type : "+eventDTO.getType()+"should have only of the fields : grade, academicSessionId, standardId");
            }
            // add a check for event existence - check for all types

        } else if(eventDTO.getType().equals(EventType.NOTE)) {
            if(!(eventDTO.getGrade() == null ^ eventDTO.getAcademicSessionId() == null ^ eventDTO.getStandardId() == null ^ eventDTO.getStudentId() == null)) {
                throw new WitcurveException("Event of type : "+eventDTO.getType()+"should have only of the fields : grade, academicSessionId, standardId, studentId");
            }
            // add a check for event existence - check for all types

        }  else if(eventDTO.getType().equals(EventType.LEAVE)) {
            if(!(eventDTO.getStudentId() == null ^ eventDTO.getStaffId() == null)) {
                throw new WitcurveException("Event of type : "+eventDTO.getType()+"should have only of the fields : studentId, staffId");
            }
            // add a check for event existence - check for all types
        }
    }

    public class EventDateAscComparator implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            if (o1.getDate().isAfter( o2.getDate())) {
                return 1;
            } else if(o1.getDate().isBefore( o2.getDate())) {
                return -1;
            } else {
                if(o1.getScd() != null && o2.getScd() != null) {
                    Integer start1 = Integer.parseInt(o1.getScd().getGsd().getStart());
                    Integer start2 = Integer.parseInt(o2.getScd().getGsd().getStart());
                    if(start1 > start2) {
                        return 1;
                    } else if(start1 < start2) {
                        return -1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        }
    }


    public class EventDateDescComparator implements Comparator<Event> {

        @Override
        public int compare(Event o1, Event o2) {
            if (o1.getDate().isAfter( o2.getDate())) {
                return -1;
            } else if(o1.getDate().isBefore( o2.getDate())) {
                return 1;
            } else {
                if(o1.getScd() != null && o2.getScd() != null) {
                    Integer start1 = Integer.parseInt(o1.getScd().getGsd().getStart());
                    Integer start2 = Integer.parseInt(o2.getScd().getGsd().getStart());
                    if(start1 > start2) {
                        return 1;
                    } else if(start1 < start2) {
                        return -1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        }
    }


}
