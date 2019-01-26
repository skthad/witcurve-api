package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.StaffType;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.repository.*;
import com.witcurve.service.AcademicSessionService;
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
import java.util.stream.Collectors;

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

    @Autowired
    AcademicSessionRepository academicSessionRepository;

    @Autowired
    LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    AcademicSessionService academicSessionService;

    private static final ArrayList<EventType> FIRST_LIST = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.TEST));

    private static final ArrayList<EventType> SECOND_LIST = new ArrayList<EventType>(
        Arrays.asList(EventType.HOLIDAY, EventType.SCHOOL_EVENT));

    private static final ArrayList<EventType> THIRD_LIST = new ArrayList<EventType>(
        Arrays.asList(EventType.HOLIDAY, EventType.ATTENDANCE));

    private static final ArrayList<EventType> LIST_FOR_STUDENT = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.SCHOOL_EVENT, EventType.TEST));

    private static final ArrayList<EventType> LIST_FOR_MONTH = new ArrayList<EventType>(
        Arrays.asList(EventType.SCHOOL_EVENT));

    private static final ArrayList<EventType> LIST_FOR_WEEK = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.SCHOOL_EVENT, EventType.TEST));

    private static final ArrayList<EventType> LIST_FOR_DIARY = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.SCHOOL_EVENT, EventType.TEST));

    private static final ArrayList<EventType> LIST_FOR_UPCOMING_EVENTS = new ArrayList<EventType>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.SCHOOL_EVENT, EventType.TEST));


    @Override
    public List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs) throws WitcurveException {
        log.debug("Request to save or update eventDTOs : {}", eventDTOs);
        isEventValid(eventDTOs);
        if (eventDTOs.size() > 1) {
            String bindingId = UUID.randomUUID().toString();
            for(EventDTO eventDTO : eventDTOs) {
                eventDTO.setBindingId(bindingId);
            }
        }
        List<Event> events = eventMapper.toEntity(eventDTOs);
        events = eventRepository.saveAll(events);
        for(int i=0;i<events.size();i++){
            if(events.get(i).getType().equals(EventType.ATTENDANCE)) {
                if(events.get(i).getStudent() != null) {
                    LocalDate date = events.get(i).getDate();
                    LeaveApplication la = leaveApplicationRepository.findLeaveForStudentOnDate(events.get(i).getStudent().getId(), date);
                    if (la != null) {
                        la.addEvents(events.get(i));
                        events.get(i).setName("LEAVE-"+la.getReason().toString());
                        events.get(i).setDescription(la.getDescription());
                    }
                }
                if(events.get(i).getStaff() != null) {
                    LocalDate date = events.get(i).getDate();
                    LeaveApplication la = leaveApplicationRepository.findLeaveForStaffOnDate(events.get(i).getStaff().getId(), date);
                    if (la != null) {
                        la.addEvents(events.get(i));
                        events.get(i).setName("LEAVE-"+la.getReason().toString());
                        events.get(i).setDescription(la.getDescription());
                    }
                }
            }
        }
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
        Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId() ;

        List<Event> events = eventRepository.findEventsByDateForStudent(eventDate, studentId, standardId, grade, schoolInfoId, LIST_FOR_STUDENT);
        Collections.sort(events, new EventDateAscComparator());

        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenDateForStaff(LocalDate eventDate, Long staffId) {
        log.debug("Request to get tests with eventDate : {} for staff with id : {} ", eventDate, staffId);

        //TODO: input eventDate may not be in the current term. So we should bring even inactive ones.
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findAllByTeacherId(staffId);
        Set<Long> standardIds = new HashSet<>();
        Set<Grade> grades = new HashSet<>();
        for(CourseTeacher courseTeacher : courseTeachers) {
            standardIds.add(courseTeacher.getStandard().getId());
            grades.add(courseTeacher.getStandard().getGrade());
        }

        //TODO: not sending session id anymore, as the query is driven by a date.
        List<Event> events = eventRepository.findEventsByDateForStaff(eventDate, staffId, standardIds, grades);
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
        Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(monthStart, monthEnd, studentId, standardId, grade, schoolInfoId, LIST_FOR_MONTH);
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);
    }

    @Override
    public List<LocalDate> findAllEventDatesOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException {

        StudentStandard studentStandard = getStudentStandardFromStudentId(studentId);
        Long standardId = studentStandard.getStandard().getId();
        Grade grade = studentStandard.getStandard().getGrade();
        Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId() ;
        LocalDate monthStart = LocalDate.of(year,month,1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        List<LocalDate> eventDates = eventRepository.findEventDatesByDateRangeForStudent(monthStart, monthEnd, studentId, standardId, grade, schoolInfoId, LIST_FOR_MONTH);
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
        Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(sunday, saturday, studentId, standardId, grade, schoolInfoId, LIST_FOR_WEEK);
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
        Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(startDate, date, studentId, standardId, grade, schoolInfoId, LIST_FOR_DIARY);
        if (events.size() > 0) {
            String lastBindingId = events.get(events.size()-1).getBindingId();
            if(lastBindingId != null) {
                List<Event> remainingList = eventRepository.findEventsByBindingId(lastBindingId, studentId, standardId, grade, schoolInfoId);
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
        Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
        List<Event> events = eventRepository.findEventsByDateRangeForStudent(date, endDate, studentId, standardId, grade, schoolInfoId, LIST_FOR_UPCOMING_EVENTS);
        if (events.size() > 0) {
            String lastBindingId = events.get(events.size()-1).getBindingId();
            if(lastBindingId != null) {
                List<Event> remainingList = eventRepository.findEventsByBindingId(lastBindingId, studentId, standardId, grade, schoolInfoId);
                if(remainingList.size() != 0) {
                    events.addAll(remainingList);
                    events = new ArrayList<>(new HashSet<>(events));
                }
            }
        }
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> findEventsByDateRangeForStaffInUpcomingEvents(LocalDate date, Long staffId) throws WitcurveException {
        log.debug("Find events for announcements for a duration of week from date : {} and for staff with id : {}", date, staffId);
        LocalDate endDate = date.plusDays(6);

        Optional<Staff> staff = staffRepository.findById(staffId);
        if (!staff.isPresent()) {
            throw new WitcurveException("Staff does not exist with id " + staffId);
        }
        SchoolInfo schoolInfo = staff.get().getSchoolInfo();

        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);

        Set<Long> standardIds = courseTeachers
            .stream()
            .map(CourseTeacher::getStandard)
            .map(s -> s.getId())
            .collect(Collectors.toSet());

        Set<Grade> grades = courseTeachers
            .stream()
            .map(CourseTeacher::getStandard)
            .map(s -> s.getGrade())
            .collect(Collectors.toSet());

        List<Event> events = eventRepository.findEventsByDateRangeForStaff(date, endDate, staffId, standardIds, grades, schoolInfo.getId(), LIST_FOR_UPCOMING_EVENTS);
        Collections.sort(events, new EventDateAscComparator());
        return eventMapper.toDto(events);

    }

    @Override
    public List<EventDTO> getAttendance(LocalDate fromDate, LocalDate toDate, Long studentId,
                                        Long standardId, Long staffId, Long schoolInfoId) throws WitcurveException {
        if (studentId == null && standardId == null && staffId == null && schoolInfoId == null) {
            throw new WitcurveException("Student ID and Standard ID both cannot be null");
        }
        //TODO find better logic condition to check if one more ids exist - use truth table;
        List<Event> attendance;
        if (studentId != null) {
            attendance = eventRepository.findAttendanceForStudent(fromDate, toDate, studentId);
        } else if(standardId != null) {
            attendance =eventRepository.findAttendanceForStandard(fromDate, toDate, standardId);
        } else if(staffId != null) {
            attendance =eventRepository.findAttendanceForStaff(fromDate, toDate, staffId);
        } else {
            attendance=eventRepository.findAttendanceForAllStaffInSchoolInfo(fromDate, toDate, schoolInfoId);
        }

        return eventMapper.toDto(attendance);
    }

    @Override
    public Page<EventDTO> getNotices(Long termId, Long studentId, Long staffId, Pageable pageable) throws WitcurveException {
        Optional<Term> term = termRepository.findById(termId);
        if(!term.isPresent()) {
            throw new WitcurveException("No term exists for id : "+termId);
        }
        Long schoolInfoId = term.get().getSession().getSchoolInfo().getId();
        LocalDate termDate = term.get().getStartDate();
        Page<Event> result = null;
        if(studentId != null) {
            List<StudentStandard> studentStandards = studentStandardRepository.getByStudentId(studentId);
            schoolInfoId= studentStandards.get(0).getStandard().getSchoolInfo().getId();
            if(studentStandards.size() !=1) {
                throw new WitcurveException("There should be an active student standard with given student id : "+studentId);
            }
            Long standardId = studentStandards.get(0).getStandard().getId();
            Grade grade = studentStandards.get(0).getStandard().getGrade();
            result = eventRepository.findStudentNotices(standardId, grade ,schoolInfoId, termDate, pageable);
        }
        if (staffId != null) {
            Optional<Staff> staff = staffRepository.findById(staffId);
            if(!staff.isPresent()) {
                throw new WitcurveException("No staff exists for id : "+ staffId);
            }
            if(staff.get().getType().equals(StaffType.TEACHING)) {
                Standard standard = standardRepository.findByClassTeacherId(staffId);
                if(standard != null) {
                   result = eventRepository.findClassTeacherNotices(standard.getId(),
                       standard.getGrade(), schoolInfoId, termDate, pageable);
                } else {
                    result = eventRepository.findTeacherNotices(schoolInfoId, termDate, pageable);
                }
            }
            // TODO: look for user role, not for staff type. 'ADMIN' is a role. It is no more a staff type
            if(staff.get().getType().equals(StaffType.NON_TEACHING)) {
                //result = eventRepository.findAdminNotices(schoolInfoId, termDate, pageable);
                List<Event> results = eventRepository.findAdminNoticesBySchoolInfoId(schoolInfoId, termDate); //pr1
                results.addAll(eventRepository.findAdminNoticesByStandardInSession(schoolInfoId, termDate));
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

    public List<EventDTO> findAllTestAndAssignmentByTeacherInWeek(Long staffId, LocalDate eventDate, ViewType type) throws WitcurveException {
        LocalDate sDate= eventDate.minusDays(6);
        List<Event> events;
        if(ViewType.ASSIGNMENT.equals(type)){
            events = eventRepository.findAssignmentsByTeacherInDateRange(staffId,sDate,eventDate);
        }
        else if(ViewType.TEST.equals(type)){
            events = eventRepository.findTestsByTeacherInDateRange(staffId,sDate,eventDate);
        }
        else {
            throw new WitcurveException("Event type should be only test and Assignment");
        }
        Collections.sort(events, new EventDateDescComparator());
        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findHolidaysInSchoolInfo(Long schoolInfoId, LocalDate startDate, LocalDate endDate) throws WitcurveException {
        log.debug("List of holidays for a schoolInfo with id : {}", schoolInfoId);

        List<Event> events = eventRepository.findByTypeAndSchoolInfoIdOrderByDateAsc(EventType.HOLIDAY, schoolInfoId, startDate, endDate);
        return eventMapper.toDto(events);
    }


    private void isEventValid(List<EventDTO> eventDTOs) throws WitcurveException {
        for(EventDTO eventDTO : eventDTOs) {
            if (!EventType.ATTENDANCE.equals(eventDTO.getType()) && eventDTO.getScd() != null && eventDTO.getCourseTeacher() != null) {
                throw new WitcurveException("Invalid request body");
            }
            if(FIRST_LIST.contains(eventDTO.getType())) {
                if(eventDTO.getType().equals(EventType.ASSIGNMENT)) {
                    if(eventDTO.getStandardId() == null) {
                        log.error("Event of type : "+eventDTO.getType()+"cannot have empty standardId for event with date");
                        throw new WitcurveException("An assignment cannot have empty standardId for event with date");
                    }
                    // add a check later

                } else {
                    if(eventDTO.getStandardId() == null || eventDTO.getScd() == null || (eventDTO.getScd() != null && eventDTO.getScd().getId() == null)) {
                        throw new WitcurveException("Event cannot have SCD without standardId");
                    }
                    Event event = eventRepository.findEventOnDateAndSlot(eventDTO.getDate(), eventDTO.getType(), eventDTO.getScd().getId());
                    if(event != null) {
                        throw new WitcurveException("IThere already exists a record for given event type and date for SCD with with id: " +eventDTO.getScd().getId());
                    }
                }
            } else if(eventDTO.getType().equals(EventType.HOLIDAY)) {
                if(eventDTO.getSchoolInfoId() == null) {
                    throw new WitcurveException("A holiday must have schoolInfoId");
                }

                List<Event> events = eventRepository.eventsBlockingHolidayAndSchoolEvents(eventDTO.getDate(), SECOND_LIST, eventDTO.getSchoolInfoId());
                events = removeExistingEvent(events, eventDTO);
                if(events.size() !=0) {
                    throw new WitcurveException("Event of type : "+eventDTO.getType()+"cannot be posted on date : "+eventDTO.getDate()+" because there is already an event of type HOLIDAY or SCHOOL_EVENT");
                }
            } else if(eventDTO.getType().equals(EventType.SCHOOL_EVENT)) {
                if(!(eventDTO.getSchoolInfoId() == null ^ eventDTO.getStandardId() == null)) {
                    throw new WitcurveException("A school event must have only one of the fields [schoolInfoId, standardId]");
                }
                Long schoolInfoId = eventDTO.getSchoolInfoId();
                if(schoolInfoId == null) {
                    Standard standard = standardRepository.findById(eventDTO.getStandardId()).get();
                    if(standard == null) {
                        throw new WitcurveException("Invalid Standard Id :"+eventDTO.getStandardId());
                    }
                    schoolInfoId = standard.getSchoolInfo().getId();

                }
                List<Event> events = eventRepository.eventsBlockingHolidayAndSchoolEvents(eventDTO.getDate(), SECOND_LIST, schoolInfoId);
                events = removeExistingEvent(events, eventDTO);
                if(events.size() !=0) {
                    throw new WitcurveException("The event clashes with an existing holiday or school event");
                }

            } else if(eventDTO.getType().equals(EventType.SUBJECT_NOTE)) {
                if(!(eventDTO.getSchoolInfoId() == null ^ eventDTO.getStandardId() == null ^ eventDTO.getStudentId() == null)) {
                    throw new WitcurveException("A subject note must have only one of the fields [schoolInfoId, standardId, studentId]");
                }

            }  else if(eventDTO.getType().equals(EventType.ATTENDANCE)) {
                if(!(eventDTO.getStudentId() == null ^ eventDTO.getStaffId() == null)) {
                    log.error("Event of type : "+eventDTO.getType()+"should have only one of the fields : studentId, staffId");
                    throw new WitcurveException("An attendance record must have only one of the fields [studentId, staffId]");
                }
                Long schoolInfoId;
                List<Event> events;
                if(eventDTO.getStudentId() != null) {
                    if(eventDTO.getStandardId() == null) {
                        throw new WitcurveException("An attendance record for student must have only standardId");
                    }
                    StudentStandard studentStandard = getStudentStandardFromStudentId(eventDTO.getStudentId());
                    schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
                    events = eventRepository.eventsBlockingLeaveForStudent(eventDTO.getDate(), THIRD_LIST, schoolInfoId, eventDTO.getStudentId());
                    events = removeExistingEvent(events, eventDTO);
                } else {
                    schoolInfoId = staffRepository.findById(eventDTO.getStaffId()).get().getSchoolInfo().getId();
                    events = eventRepository.eventsBlockingLeaveForStaff(eventDTO.getDate(), THIRD_LIST, schoolInfoId, eventDTO.getStaffId());
                    events = removeExistingEvent(events, eventDTO);
                }
                if(events.size() !=0) {
                    throw new WitcurveException("An attendance record cannot be posted on a holiday or school event");
                }
            } else if(eventDTO.getType().equals(EventType.NOTICE) || eventDTO.getType().equals(EventType.STAFF_NOTICE)) {
                if(eventDTO.getType().equals(EventType.NOTICE)) {
                    if(!(eventDTO.getSchoolInfoId() == null ^ eventDTO.getStandardId() == null)) {
                        throw new WitcurveException("A notice must have only one of the fields [schoolInfoId, standardId]");
                    }
                    Long schoolInfoId = eventDTO.getSchoolInfoId();
                    if(schoolInfoId == null) {
                        Optional<Standard> standard = standardRepository.findById(eventDTO.getStandardId());
                        if(!standard.isPresent()) {
                            throw new WitcurveException("No standard found with id :"+eventDTO.getStandardId());
                        }
                    }
                } else {
                    if(eventDTO.getSchoolInfoId() ==null) {
                        throw new WitcurveException("A staff event must have schoolInfoId");
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
                    String start1 = o1.getScd().getGsd().getStart();
                    String start2 = o2.getScd().getGsd().getStart();
                    return start1.compareTo(start2);
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
                    String start1 = o1.getScd().getGsd().getStart();
                    String start2 = o2.getScd().getGsd().getStart();
                    return start1.compareTo(start2);
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

    private List<Event> removeExistingEvent(List<Event> events, EventDTO eventDTO) {
        if(eventDTO.getId() != null) {
            Event event = new Event();
            event.setId(eventDTO.getId());
            events.remove(event);
        }
        return events;
    }

}
