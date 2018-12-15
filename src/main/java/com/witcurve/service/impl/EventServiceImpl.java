package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.StaffType;
import com.witcurve.repository.*;
import com.witcurve.service.EventService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    TermRepository termRepository;

    @Autowired
    CourseTeacherRepository courseTeacherRepository;

    @Autowired
    StaffRepository staffRepository;

    private static final ArrayList<EventType> FIRST_LIST = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.EXAM, EventType.TEST));

    private static final ArrayList<EventType> SECOND_LIST = new ArrayList<EventType>(
        Arrays.asList(EventType.HOLIDAY, EventType.SCHOOL_EVENT));

    private static final ArrayList<EventType> THIRD_LIST = new ArrayList<EventType>(
        Arrays.asList(EventType.HOLIDAY, EventType.ATTENDANCE));

    private static final ArrayList<EventType> LIST_FOR_STUDENT = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.SCHOOL_EVENT, EventType.TEST));

    private static final ArrayList<EventType> LIST_FOR_MONTH = new ArrayList<EventType>(
        Arrays.asList(EventType.EXAM, EventType.SCHOOL_EVENT));

    private static final ArrayList<EventType> LIST_FOR_WEEK = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.SCHOOL_EVENT, EventType.TEST));

    private static final ArrayList<EventType> LIST_FOR_DIARY = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.EXAM, EventType.SCHOOL_EVENT, EventType.TEST));

    private static final ArrayList<EventType> LIST_FOR_UPCOMING_EVENTS = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.EXAM, EventType.SCHOOL_EVENT, EventType.TEST));


    @Override
    public List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs) throws WitcurveException {
        log.debug("Request to save or update eventDTOs : {}", eventDTOs);
        isEventValid(eventDTOs);
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
    public List<EventDTO> findAllEventsOnGivenDateForStudent(LocalDate eventDate, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with eventDate : {} for student with id : {}", eventDate, studentId);

        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();

        List<Event> events = eventRepository.findEventsByDateForStudent(eventDate, studentId, standardId, grade, sessionId, LIST_FOR_STUDENT);
        Collections.sort(events, new EventDateAscComparator());

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenDateForStaff(LocalDate eventDate, Long staffId, Long termId) throws WitcurveException {
        log.debug("Request to get tests with eventDate : {} for staff with id : {} for term with id : {}", eventDate, staffId, termId);

        Optional<Term> termOptional = termRepository.findById(termId);
        if(!termOptional.isPresent()) {
            throw new WitcurveException("Term doesn't exist with given term id : "+termId);
        }
        Long sessionId = termOptional.get().getSession().getId();
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherIdAndTermId(staffId, termId);
        Set<Long> standardIds = new HashSet<>();
        Set<Grade> grades = new HashSet<>();
        for(CourseTeacher courseTeacher : courseTeachers) {
            standardIds.add(courseTeacher.getStandard().getId());
            grades.add(courseTeacher.getStandard().getGrade());
        }
        List<Event> events = eventRepository.findEventsByDateForStaff(eventDate, staffId, standardIds, grades, sessionId);
        Collections.sort(events, new EventDateAscComparator());

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with month no. : {} of year : {} or student with id : {}", month, year, studentId);
        // need to get Events of type Holiday, Leave, Exam, SchoolEvent

        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(monthStart, monthEnd, studentId, standardId, grade, sessionId, LIST_FOR_MONTH);
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);
    }

    @Override
    public List<LocalDate> findAllEventDatesOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException {

        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<LocalDate> eventDates = eventRepository.findEventDatesByDateRangeForStudent(monthStart, monthEnd, studentId, standardId, grade, sessionId, LIST_FOR_MONTH);
        return eventDates;
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

        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(sunday, saturday, studentId, standardId, grade, sessionId, LIST_FOR_WEEK);
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> findAllEventsForDiary(LocalDate date, Long studentId) throws WitcurveException {
        log.debug("Find events for diary for a duration of week from date : {} and for student with id : {}", date, studentId);
        LocalDate startDate = date.minusDays(6);

        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(startDate, date, studentId, standardId, grade, sessionId, LIST_FOR_DIARY);
        if (events.size() > 0) {
            String lastBindingId = events.get(events.size()-1).getBindingId();
            if(lastBindingId != null) {
                List<Event> remainingList = eventRepository.findEventsByBindingId(lastBindingId, studentId, standardId, grade, sessionId);
                if(remainingList.size() != 0) {
                    events.addAll(remainingList);
                    events = new ArrayList<>(new HashSet<>(events));
                }
            }
        }
        Collections.sort(events, new EventDateDescComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> findEventsByDateRangeForStudentInUpcomingEvents(LocalDate date, Long studentId) throws WitcurveException {
        log.debug("Find events for announcements for a duration of week from date : {} and for student with id : {}", date, studentId);
        LocalDate endDate = date.plusDays(6);

        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(date, endDate, studentId, standardId, grade, sessionId, LIST_FOR_UPCOMING_EVENTS);
        if (events.size() > 0) {
            String lastBindingId = events.get(events.size()-1).getBindingId();
            if(lastBindingId != null) {
                List<Event> remainingList = eventRepository.findEventsByBindingId(lastBindingId, studentId, standardId, grade, sessionId);
                if(remainingList.size() != 0) {
                    events.addAll(remainingList);
                    events = new ArrayList<>(new HashSet<>(events));
                }
            }
        }
        Collections.sort(events, new EventDateDescComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> getAllLeavesForStudent(LocalDate date, Long studentId) throws WitcurveException {
        log.debug("Find events for leaves for a student with id : {}", studentId);


        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long sessionId = studentStandard.getStandard().getTerm().getSession().getId();

        List<Event> events;
        if (date == null) { // get all leaves info for the current term

            //TODO change the implementation to have only those events associated with leave application
            events = eventRepository.findAllLeavesForStudentInSession(studentId, sessionId);
        } else { // get leave data for a day
            events = eventRepository.findLeaveForStudent(date, studentId);
        }

        if (events != null && events.size() > 0) {
            String lastBindingId = events.get(events.size()-1).getBindingId();
            if(lastBindingId != null) {
                List<Event> remainingList = eventRepository.findEventsByBindingId(lastBindingId, studentId, standardId, grade, sessionId);
                if(remainingList.size() != 0) {
                    events.addAll(remainingList);
                    events = new ArrayList<>(new HashSet<>(events));
                }
            }
            Collections.sort(events, new EventDateDescComparator());
        }
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> getAllLeavesForStandard(LocalDate date, Long standardId) throws WitcurveException {
        log.debug("Find events for leaves for a standard with id : {}", standardId);

        Optional<Standard> result = standardRepository.findById(standardId);

        if(!result.isPresent()) {
            throw new WitcurveException("There is no standard with given standard id : " + standardId);
        }

        Standard standard = result.get();

        Long sessionId = standard.getTerm().getSession().getId();

        List<Event> events = new ArrayList<>();
        if (date == null) { // get all leaves info for the current term

            //TODO change the implementation to have only those events associated with leave application
            events = eventRepository.findLeavesForStandard(standardId, sessionId);
        } else { // get attendance data for a day
            events = eventRepository.findAttendanceForStandard(date, standardId);
        }

        Collections.sort(events, new EventDateDescComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> getAttendance(LocalDate date, Long studentId, Long standardId) throws WitcurveException {
        if (studentId == null && standardId == null) {
            throw new WitcurveException("Student ID and Standard ID both cannot be null");
        }
        List<Event> attendance = null;
        if (studentId != null) {
            attendance = eventRepository.findAttendanceForStudent(date, studentId);
        } else {
            attendance =eventRepository.findAttendanceForStandard(date, standardId);
        }

        return eventMapper.toDto(attendance);
    }

    @Override
    public Page<EventDTO> getNotices(Long termId, Long studentId, Long staffId, Pageable pageable) throws WitcurveException {
        Optional<Term> term = termRepository.findById(termId);
        if(!term.isPresent()) {
            throw new WitcurveException("No term exists for id : "+termId);
        }
        Long sessionId = term.get().getSession().getId();
        Page<Event> result = null;
        if(studentId != null) {
            List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
            if(studentStandards.size() !=1) {
                throw new WitcurveException("There should be an active student standard with given student id : "+studentId);
            }
            Long standardId = studentStandards.get(0).getStandard().getId();
            Grade grade = studentStandards.get(0).getStandard().getGrade();
            result = eventRepository.findStudentNotices(standardId, grade ,sessionId, pageable);
        }
        if (staffId != null) {
            Optional<Staff> staff = staffRepository.findById(staffId);
            if(!staff.isPresent()) {
                throw new WitcurveException("No staff exists for id : "+ staffId);
            }
            if(staff.get().getType().equals(StaffType.TEACHING)) {
                Standard standard = standardRepository.findByClassTeacherIdAndTermId(staffId, termId);
                if(standard != null) {
                   result = eventRepository.findClassTeacherNotices(standard.getId(),
                       standard.getGrade(), sessionId, pageable);
                } else {
                    result = eventRepository.findTeacherNotices(sessionId, pageable);
                }
            }
            if(staff.get().getType().equals(StaffType.ADMIN)) {
                List<Event> results = eventRepository.findAdminNoticesBySession(sessionId);
                results.addAll(eventRepository.findAdminNoticesByStandardInSession(sessionId));
                Collections.sort(results, new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        return o1.getDate().isAfter(o2.getDate()) ? -1 : 0;
                    }
                });

                Integer resultSize = results.size();
                // offset  2, size 10
                // from = 20, to = 30
                // resultSize = 24, 30, 34
                // to========== 24, 30, 30
                // from======== 20, 20, 20
                Integer offset = Integer.parseInt(String.valueOf(pageable.getOffset()));
                Integer size = Integer.parseInt(String.valueOf(pageable.getPageSize()));
                Integer from =  offset * size;
                Integer to = from + size;

                if (from >= resultSize) {
                    results = results.subList(0, 0);
                } else if (resultSize >= to) {
                    results = results.subList(from, to);
                } else {
                    results.subList(from, resultSize);
                }

                return new PageImpl<>(eventMapper.toDto(results));
            }
        }

        return result.map(eventMapper::toDto);
    }

    private void isEventValid(List<EventDTO> eventDTOs) throws WitcurveException {
        for(EventDTO eventDTO : eventDTOs) {
            if (eventDTO.getScd() != null && eventDTO.getCourseTeacher() != null) {
                throw new WitcurveException("Invalid request body");
            }
            if(FIRST_LIST.contains(eventDTO.getType())) {
                if(eventDTO.getType().equals(EventType.ASSIGNMENT)) {
                    if(eventDTO.getStandardId() == null) {
                        log.error("Event of type : "+eventDTO.getType()+"cannot have empty standardId for event with date");
                        throw new WitcurveException("Invalid request body");
                    }
                    // add a check later

                } else {
                    if(eventDTO.getStandardId() == null || eventDTO.getScd() == null || (eventDTO.getScd() != null && eventDTO.getScd().getId() == null)) {
                        log.error("Event of type : "+eventDTO.getType()+"cannot have empty standardId and scd");
                        throw new WitcurveException("Invalid request body");
                    }
                    Event event = eventRepository.findEventOnDateAndSlot(eventDTO.getDate(), eventDTO.getType(), eventDTO.getScd().getId());
                    if(event != null) {
                        log.error("There already exists a record for given event type : "+eventDTO.getType()+" for scd with id : "+eventDTO.getScd().getId()+ " on date : "+eventDTO.getDate().toString());
                        throw new WitcurveException("Invalid request body");
                    }
                }
            } else if(eventDTO.getType().equals(EventType.HOLIDAY)) {
                if(eventDTO.getAcademicSessionId() == null) {
                    log.error("Event of type : "+eventDTO.getType()+"should have the field : academicSessionId");
                    throw new WitcurveException("Invalid request body");
                }

                List<Event> events = eventRepository.eventsBlockingHolidayAndSchoolEvents(eventDTO.getDate(), SECOND_LIST, eventDTO.getAcademicSessionId());
                if(events.size() !=0) {
                    log.error("Event of type : "+eventDTO.getType()+"cannot be posted on date : "+eventDTO.getDate()+" because there is already an event of type HOLIDAY or SCHOOL_EVENT");
                    throw new WitcurveException("Invalid request body");
                }
            } else if(eventDTO.getType().equals(EventType.SCHOOL_EVENT)) {
                if(!(eventDTO.getAcademicSessionId() == null ^ eventDTO.getStandardId() == null)) {
                    log.error("Event of type : "+eventDTO.getType()+"should have one of the fields : academicSessionId, standardId");
                    throw new WitcurveException("Invalid request body");
                }
                Long sessionId = eventDTO.getAcademicSessionId();
                if(sessionId == null) {
                    Standard standard = standardRepository.findById(eventDTO.getStandardId()).get();
                    if(standard == null) {
                        throw new WitcurveException("Invalid Standard Id :"+eventDTO.getStandardId());
                    }
                    sessionId = standard.getTerm().getSession().getId();

                }
                List<Event> events = eventRepository.eventsBlockingHolidayAndSchoolEvents(eventDTO.getDate(), SECOND_LIST, sessionId);
                if(events.size() !=0) {
                    log.error("Event of type : "+eventDTO.getType()+"cannot be posted on date : "+eventDTO.getDate()+" because there is already an event of type HOLIDAY or SCHOOL_EVENT");
                    throw new WitcurveException("Invalid request body");
                }

            } else if(eventDTO.getType().equals(EventType.SUBJECT_NOTE)) {
                if(!(eventDTO.getAcademicSessionId() == null ^ eventDTO.getStandardId() == null ^ eventDTO.getStudentId() == null)) {
                    log.error("Event of type : "+eventDTO.getType()+"should have only of the fields : academicSessionId, standardId, studentId");
                    throw new WitcurveException("Invalid request body");
                }

            }  else if(eventDTO.getType().equals(EventType.ATTENDANCE)) {
                if(!(eventDTO.getStudentId() == null ^ eventDTO.getStaffId() == null)) {
                    log.error("Event of type : "+eventDTO.getType()+"should have only one of the fields : studentId, staffId");
                    throw new WitcurveException("Invalid request body");
                }
                Long sessionId = null;
                List<Event> events = new ArrayList<>();
                if(eventDTO.getStudentId() != null) {

                    StudentStandard studentStandard = getStudentStandardFromStudentId(eventDTO.getStudentId());
                    sessionId = studentStandard.getStandard().getTerm().getSession().getId();
                    events = eventRepository.eventsBlockingLeave(eventDTO.getDate(), THIRD_LIST, sessionId, eventDTO.getStudentId());
                } else {
                    // get session id for staff - I think it would be better to have both staff id and session id for leave.

                }
                if(events.size() !=0) {
                    log.error("Event of type : "+eventDTO.getType()+"cannot be posted on date : "+eventDTO.getDate()+" because there is already an event of type HOLIDAY or SCHOOL_EVENT or LEAVE");
                    throw new WitcurveException("Invalid request body");
                }
            } else if(eventDTO.getType().equals(EventType.NOTICE) || eventDTO.getType().equals(EventType.STAFF_NOTICE)) {
                if(eventDTO.getType().equals(EventType.NOTICE)) {
                    if(!(eventDTO.getAcademicSessionId() == null ^ eventDTO.getStandardId() == null)) {
                        log.error("Event of type : "+eventDTO.getType()+"should have one of the fields : academicSessionId, standardId");
                        throw new WitcurveException("Invalid request body");
                    }
                    Long sessionId = eventDTO.getAcademicSessionId();
                    if(sessionId == null) {
                        Standard standard = standardRepository.findById(eventDTO.getStandardId()).get();
                        if(standard == null) {
                            throw new WitcurveException("Invalid Standard Id :"+eventDTO.getStandardId());
                        }
                    }
                } else {
                    if(eventDTO.getAcademicSessionId() ==null) {
                        log.error("Event of type : "+eventDTO.getType()+"should have the fields : academicSessionId");
                        throw new WitcurveException("Invalid request body");
                    }
                }
            }
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

    private StudentStandard getStudentStandardFromStudentId(Long studentId) throws WitcurveException {

        StudentStandard studentStandard = null;
        List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
        if(studentStandards.isEmpty()) {
            throw new WitcurveException("There is no student standard with given student id : "+studentId);
        } else if (studentStandards.size() > 1) {
            throw new WitcurveException("There are more than one active student standard with given student id : "+studentId);
        } else {
            studentStandard = studentStandards.get(0);
        }
        return studentStandard;
    }

}
