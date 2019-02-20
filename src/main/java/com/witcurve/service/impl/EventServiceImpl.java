package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.EventType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.StaffType;
import com.witcurve.domain.enumeration.ViewType;
import com.witcurve.repository.*;
import com.witcurve.service.EventService;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
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
    LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    StudentStandardService studentStandardService;

    @Autowired
    SlotCourseDetailsService slotCourseDetailsService;

    private static final ArrayList<EventType> FIRST_LIST = new ArrayList<>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.TEST));

    private static final ArrayList<EventType> SECOND_LIST = new ArrayList<>(
        Arrays.asList(EventType.HOLIDAY, EventType.SCHOOL_EVENT));

    private static final ArrayList<EventType> THIRD_LIST = new ArrayList<>(
        Arrays.asList(EventType.HOLIDAY, EventType.ATTENDANCE));

    private static final ArrayList<String> LIST_FOR_DAY = new ArrayList<>(
        Arrays.asList(EventType.DAILY_UPDATE.toString(), EventType.TEST.toString()));

    private static final ArrayList<String> LIST_FOR_DATE_RANGE = new ArrayList<>(
        Arrays.asList(EventType.TEST.toString()));


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
                    List<LeaveApplication> la = leaveApplicationRepository.findByStudentId(events.get(i).getStudent().getId(), date, date);
                    if (la.size() > 0) {
                        la.get(0).addEvents(events.get(i));
                        events.get(i).setName("LEAVE-"+la.get(0).getReason().toString());
                        events.get(i).setDescription(la.get(0).getDescription());
                    }
                }
                if(events.get(i).getStaff() != null) {
                    LocalDate date = events.get(i).getDate();
                    List<LeaveApplication> la = leaveApplicationRepository.findByStaffId(events.get(i).getStaff().getId(), date, date);
                    if (la.size() > 0) {
                        la.get(0).addEvents(events.get(i));
                        events.get(i).setName("LEAVE-"+la.get(0).getReason().toString());
                        events.get(i).setDescription(la.get(0).getDescription());
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
        log.debug("Request to get events with eventDate : {} for student with id : {}", eventDate, studentId);
        List<Event> result = null;
        StudentStandardDTO studentStandard = studentStandardService.getByStudentId(studentId);
        if(studentStandard != null) {
            Long standardId = studentStandard.getStandard().getId();
            Grade grade = studentStandard.getStandard().getGrade();
            Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId() ;

            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStudent(eventDate, eventDate, studentId, standardId, grade.toString(), schoolInfoId, LIST_FOR_DAY);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateAscComparator());
        }

        return eventMapper.toDto(result);
    }

    @Override
    public List<EventDTO> findAllDiaryEventsForStudent(LocalDate eventDate, Long studentId) throws WitcurveException {
        log.debug("Request to get diary events with eventDate : {} for student with id : {}", eventDate, studentId);
        List<Event> result = null;
        StudentStandardDTO studentStandard = studentStandardService.getByStudentId(studentId);
        if(studentStandard != null) {
            Long standardId = studentStandard.getStandard().getId();
            Grade grade = studentStandard.getStandard().getGrade();
            Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId() ;

            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStudent(eventDate.minusDays(6), eventDate, studentId, standardId, grade.toString(), schoolInfoId, LIST_FOR_DAY);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateDescComparator());
        }

        return eventMapper.toDto(result);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenDateForStaff(LocalDate eventDate, Long staffId) throws WitcurveException {
        log.debug("Request to get tests with eventDate : {} for staff with id : {} ", eventDate, staffId);
        List<Event> result = null;

        List<CourseTeacher> courseTeachers = courseTeacherRepository.findAllByTeacherId(staffId);
        if(courseTeachers.size() !=0) {
            Set<Long> standardIds = new HashSet<>();
            Set<String> grades = new HashSet<>();
            for(CourseTeacher courseTeacher : courseTeachers) {
                standardIds.add(courseTeacher.getStandard().getId());
                grades.add(courseTeacher.getStandard().getGrade().toString());
            }
            Optional<Staff> staff = staffRepository.findById(staffId);
            if (!staff.isPresent()) {
                throw new WitcurveException("No staff found with ID: " + staffId);
            }
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(eventDate, eventDate, staffId, standardIds, grades, staff.get().getSchoolInfo().getId(), LIST_FOR_DAY);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateAscComparator());
        } else {
            //need to get school info events
           // result =   eventRepository.findSchoolInfoEventsByDateRange(eventDate, eventDate, staffId, standardIds, grades, staff.get().getSchoolInfo().getId(), LIST_FOR_DATE_RANGE);
            Collections.sort(result, new EventDateAscComparator());
        }

        return eventMapper.toDto(result);
    }

    private List<Long> convertBigIntToLong(List<BigInteger> list) {
        List<Long> result = new ArrayList<>();
        for (BigInteger num : list) {
            result.add(num.longValue());
        }
        return result;
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException {
        log.debug("Request to get tests with month no. : {} of year : {} or student with id : {}", month, year, studentId);
        List<Event> result = new ArrayList<>();

        StudentStandardDTO studentStandard = studentStandardService.getByStudentId(studentId);
        if (studentStandard != null) {
            Long standardId = studentStandard.getStandard().getId();
            Grade grade = studentStandard.getStandard().getGrade();
            Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
            LocalDate monthStart = LocalDate.of(year,month,1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStudent(monthStart, monthEnd, studentId, standardId, grade.toString(), schoolInfoId, LIST_FOR_DATE_RANGE);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateAscComparator());
        }
        return eventMapper.toDto(result);
    }

    @Override
    public List<EventDTO> findAllEventsOnGivenMonthForStaff(Integer month, Integer year, Long staffId) throws WitcurveException {
        log.debug("Request to get tests with month no. : {} of year : {} or student with id : {}", month, year, staffId);
        // need to get Events of type Holiday, Leave, Exam, SchoolEvent
        List<Event> result = new ArrayList<>();

        Optional<Staff> staff = staffRepository.findById(staffId);
        if (!staff.isPresent()) {
            throw new WitcurveException("Staff does not exist with id " + staffId);
        }
        SchoolInfo schoolInfo = staff.get().getSchoolInfo();

        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);

        if(courseTeachers.size() !=0) {
            Set<Long> standardIds = courseTeachers
                .stream()
                .map(CourseTeacher::getStandard)
                .map(s -> s.getId())
                .collect(Collectors.toSet());

            Set<String> grades = courseTeachers
                .stream()
                .map(CourseTeacher::getStandard)
                .map(s -> s.getGrade().toString())
                .collect(Collectors.toSet());
            LocalDate monthStart = LocalDate.of(year,month,1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(monthStart, monthEnd, staffId, standardIds, grades, schoolInfo.getId(), LIST_FOR_DATE_RANGE);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateAscComparator());
        } else {
            //result = eventRepository.findSchoolInfoEventsByDateRange(monthStart, monthEnd, staffId, standardIds, grades, schoolInfo.getId(), LIST_FOR_DATE_RANGE);
            Collections.sort(result, new EventDateAscComparator());
        }
        return eventMapper.toDto(result);
    }

    @Override
    public List<LocalDate> findAllEventDatesOnGivenMonthForStudent(Integer month, Integer year, Long studentId) throws WitcurveException {
        List<LocalDate> eventDates = new ArrayList<>();
        StudentStandardDTO studentStandard = studentStandardService.getByStudentId(studentId);
        if(studentStandard != null) {
            Long standardId = studentStandard.getStandard().getId();
            Grade grade = studentStandard.getStandard().getGrade();
            Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId() ;
            LocalDate monthStart = LocalDate.of(year,month,1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            eventDates = eventRepository.findEventDatesByDateRangeForStudent(monthStart, monthEnd, studentId, standardId, grade.toString(), schoolInfoId, LIST_FOR_DATE_RANGE);
        }
        return eventDates;
    }

    @Override
    public List<EventDTO> findEventsByDateRangeForStudentInUpcomingEvents(LocalDate date, Long studentId) throws WitcurveException {
        log.debug("Find events for announcements for a duration of week from date : {} and for student with id : {}", date, studentId);
        LocalDate endDate = date.plusDays(6);
        List<Event> result = new ArrayList<>();
        StudentStandardDTO studentStandard = studentStandardService.getByStudentId(studentId);
        if(studentStandard != null) {
            Long standardId = studentStandard.getStandard().getId();
            Grade grade = studentStandard.getStandard().getGrade();
            Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStudent(date, endDate, studentId, standardId, grade.toString(), schoolInfoId, LIST_FOR_DATE_RANGE);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateAscComparator());
        }

        return eventMapper.toDto(result);

    }

    @Override
    public List<EventDTO> findEventsByDateRangeForStaffInUpcomingEvents(LocalDate date, Long staffId) throws WitcurveException {
        log.debug("Find events for announcements for a duration of week from date : {} and for staff with id : {}", date, staffId);
        LocalDate endDate = date.plusDays(6);
        List<Event> result = new ArrayList<>();

        Optional<Staff> staff = staffRepository.findById(staffId);
        if (!staff.isPresent()) {
            throw new WitcurveException("Staff does not exist with id " + staffId);
        }
        SchoolInfo schoolInfo = staff.get().getSchoolInfo();
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);

        if(courseTeachers.size() !=0) {
            Set<Long> standardIds = courseTeachers
                .stream()
                .map(CourseTeacher::getStandard)
                .map(s -> s.getId())
                .collect(Collectors.toSet());

            Set<String> grades = courseTeachers
                .stream()
                .map(CourseTeacher::getStandard)
                .map(s -> s.getGrade().toString())
                .collect(Collectors.toSet());

            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(date, endDate, staffId, standardIds, grades, schoolInfo.getId(), LIST_FOR_DATE_RANGE);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateAscComparator());
        } else {

        }


        return eventMapper.toDto(result);

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
    public Page<EventDTO> getNotices(LocalDate startDate, LocalDate endDate, Long studentId, Long staffId, Pageable pageable) throws WitcurveException {
        Page<Event> result = null;
        Long schoolInfoId;
        if(studentId != null) {
            StudentStandardDTO studentStandard = studentStandardService.getByStudentId(studentId);
            if(studentStandard != null) {
                schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
                Long standardId = studentStandard.getStandard().getId();
                Grade grade = studentStandard.getStandard().getGrade();
                result = eventRepository.findStudentNotices(standardId, grade ,schoolInfoId, startDate, endDate, pageable);
            }
        }
        if (staffId != null) {
            Optional<Staff> staff = staffRepository.findById(staffId);
            if(!staff.isPresent()) {
                throw new WitcurveException("No staff exists for id : "+ staffId);
            }
            schoolInfoId = staff.get().getSchoolInfo().getId();
            if(staff.get().getType().equals(StaffType.TEACHING)) {
                Standard standard = standardRepository.findByClassTeacherId(staffId);
                if(standard != null) {
                   result = eventRepository.findClassTeacherNotices(standard.getId(),
                       standard.getGrade(), schoolInfoId, startDate, endDate, pageable);
                } else {
                    result = eventRepository.findTeacherNotices(schoolInfoId, startDate, endDate, pageable);
                }
            }
            // TODO: look for user role, not for staff type. 'ADMIN' is a role. It is no more a staff type
            //TODO check if admin notices work properly
            if(staff.get().getType().equals(StaffType.NON_TEACHING)) {
                  result= eventRepository.findAdminNoticesBySchoolInfoId(schoolInfoId, startDate, endDate, pageable); //pr1
//                Collections.sort(results, (o1, o2) -> o1.getDate().isAfter(o2.getDate()) ? -1 : 0);
//
//                Integer resultSize = results.size();
//                // offset  2, size 10
//                // from = 20, to = 30
//                // resultSize = 24, 30, 34
//                // to========== 24, 30, 30
//                // from======== 20, 20, 20
//                Integer offset = Integer.parseInt(String.valueOf(pageable.getOffset()));
//                Integer size = Integer.parseInt(String.valueOf(pageable.getPageSize()));
//                Integer from =  offset * size;
//                Integer to = from + size;
//
//                if (from >= resultSize) {
//                    results = results.subList(0, 0);
//                } else if (resultSize >= to) {
//                    results = results.subList(from, to);
//                } else {
//                    results.subList(from, resultSize);
//                }
//
//                return eventMapper.toDto(results);
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
//                    List<SlotCourseDetailsDTO> slotCourseDetails = slotCourseDetailsService.getSlotCourseDetailsByStandardId(eventDTO.getStandardId());
//                    if(!slotCourseDetails.contains(eventDTO.getScd())) {
//                        throw new WitcurveException("This scd doesn't belong to given standard id");
//                    }
                    Event event = eventRepository.findEventOnDateAndSlot(eventDTO.getDate(), eventDTO.getType(), eventDTO.getScd().getId());
                    if(event != null && !event.getId().equals(eventDTO.getId())) {
                        throw new WitcurveException("There already exists a record for given event type and date for SCD with with id: " +eventDTO.getScd().getId());
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
                    StudentStandardDTO studentStandard = studentStandardService.getByStudentId(eventDTO.getStudentId());
                    if(studentStandard == null) {
                        throw new WitcurveException("Student with id :"+eventDTO.getStudentId()+" not mapped to this standard id, so attendance cannot be created");
                    }
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
                if (eventDTO.getStandardId() != null) {
                    Optional<Standard> standard = standardRepository.findById(eventDTO.getStandardId());
                    if(!standard.isPresent()) {
                        throw new WitcurveException("No standard found with id :" + eventDTO.getStandardId());
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

    private List<Event> removeExistingEvent(List<Event> events, EventDTO eventDTO) {
        if(eventDTO.getId() != null) {
            Event event = new Event();
            event.setId(eventDTO.getId());
            events.remove(event);
        }
        return events;
    }

}
