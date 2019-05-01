package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.*;
import com.witcurve.service.EventService;
import com.witcurve.service.SlotCourseDetailsService;
import com.witcurve.service.StudentStandardService;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.service.util.WitcurveUtil;
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

    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    StudentRepository studentRepository;

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
            for(EventDTO eventDTO : eventDTOs) {
                if(eventDTO.getKeywords()!= null) {
                    for (String keyword : eventDTO.getKeywords()) {
                        keywordRepository.save(new Keyword(keyword));
                    }
                }
            }
        } else {
            if(eventDTOs.get(0).getKeywords()!= null) {
                for (String keyword : eventDTOs.get(0).getKeywords()) {
                    keywordRepository.save(new Keyword(keyword));
                }
            }
        }

        List<Event> events = eventMapper.toEntity(eventDTOs);
        for(Event event : events){
            if(event.getType().equals(EventType.ATTENDANCE)) {
                if (event.getAttendanceType() == null) {
                    event.setAttendanceType(AttendanceType.PRESENT);
                }
                if(event.getStudent() != null) {
                    LocalDate date = event.getDate();
                    List<LeaveApplication> la = leaveApplicationRepository.findByStudentId(event.getStudent().getId(), date, date);
                    if (la.size() > 0) {
                        la.get(0).addEvents(event);
                        event.setName("LEAVE-"+la.get(0).getReason().toString());
                        event.setDescription(la.get(0).getDescription());
                    }
                }
                if(event.getStaff() != null) {
                    LocalDate date = event.getDate();
                    List<LeaveApplication> la = leaveApplicationRepository.findByStaffId(event.getStaff().getId(), date, date);
                    if (la.size() > 0) {
                        la.get(0).addEvents(event);
                        event.setName("LEAVE-"+la.get(0).getReason().toString());
                        event.setDescription(la.get(0).getDescription());
                    }
                }
            }
        }
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
        List<Event> result = new ArrayList<>();
        List<CourseTeacher> courseTeachers = courseTeacherRepository.findAllByTeacherId(staffId);
        if(courseTeachers.size() !=0) {
            Set<Long> standardIds = new HashSet<>();
            Set<String> grades = new HashSet<>();
            Set<Long> courseTeacherIds = new HashSet<>();
            for(CourseTeacher courseTeacher : courseTeachers) {
                standardIds.add(courseTeacher.getStandard().getId());
                grades.add(courseTeacher.getStandard().getGrade().toString());
                courseTeacherIds.add(courseTeacher.getId());
            }
            Optional<Staff> staff = staffRepository.findById(staffId);
            if (!staff.isPresent()) {
                throw new WitcurveException("No staff found with ID: " + staffId);
            }
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(eventDate, eventDate, staffId, courseTeacherIds, standardIds, grades, staff.get().getSchoolInfo().getId(), LIST_FOR_DAY);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
            Collections.sort(result, new EventDateAscComparator());
        } else {
            //need to get school info events
           // result =   eventRepository.findSchoolInfoEventsByDateRange(eventDate, eventDate, staffId, standardIds, grades, staff.get().getSchoolInfo().getId(), LIST_FOR_DATE_RANGE);
          //  Collections.sort(result, new EventDateAscComparator());
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

            Set<Long> courseTeacherIds = courseTeachers
                .stream()
                .map(CourseTeacher::getId)
                .collect(Collectors.toSet());

            Set<String> grades = courseTeachers
                .stream()
                .map(CourseTeacher::getStandard)
                .map(s -> s.getGrade().toString())
                .collect(Collectors.toSet());

            LocalDate monthStart = LocalDate.of(year,month,1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(monthStart, monthEnd, staffId, courseTeacherIds, standardIds, grades, schoolInfo.getId(), LIST_FOR_DATE_RANGE);
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
    public List<EventDTO> findUpcomingEventsForStudentsInWeek(LocalDate date, Long studentId) throws WitcurveException {
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
    public List<EventDTO> findUpcomingEventsForStaffInWeek(LocalDate date, Long staffId) throws WitcurveException {
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

            Set<Long> courseTeacherIds = courseTeachers
                .stream()
                .map(CourseTeacher::getId)
                .collect(Collectors.toSet());

            Set<String> grades = courseTeachers
                .stream()
                .map(CourseTeacher::getStandard)
                .map(s -> s.getGrade().toString())
                .collect(Collectors.toSet());

            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(date, endDate, staffId, courseTeacherIds, standardIds, grades, schoolInfo.getId(), LIST_FOR_DATE_RANGE);
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
        WitcurveUtil.correctDateFormat(fromDate, toDate);
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
    public Page<EventDTO> getNotices(LocalDate startDate, LocalDate endDate, Long userId, List<Long> standardIds, List<String> keywords, Long schoolInfoId, Pageable pageable) throws WitcurveException {

        WitcurveUtil.correctDateFormat(startDate, endDate);

        Page<Event> result = null;

        Student student = studentRepository.getStudentByUserId(userId);
        if (student != null) {
            StudentStandardDTO studentStandard = studentStandardService.getByStudentId(student.getId());
            if (studentStandard != null) {
                if (!schoolInfoId.equals(student.getSchoolInfo().getId())) {
                throw new WitcurveException("Given user does not belong to this board");
            }
            Long standardId = studentStandard.getStandard().getId();
            Grade grade = studentStandard.getStandard().getGrade();
            result = eventRepository.findStudentNotices(standardId, grade, schoolInfoId, startDate, endDate, pageable);
        } else {
            throw new WitcurveException("Given user does not belong to any standard");
        }
            return result.map(eventMapper::toDto);
        }

        Staff staff = staffRepository.getStaffByUserId(userId);
        if (staff != null) {
            if (!schoolInfoId.equals(staff.getSchoolInfo().getId())) {
                throw new WitcurveException("Given user does not belong to this board");
            }
            if (staff.getType().equals(StaffType.TEACHING)) {
                Standard standard = standardRepository.findByClassTeacherId(staff.getId());
                if(standard != null) {
                    if (keywords != null && keywords.size() > 0) {
                        result = eventRepository.findClassTeacherNoticesByKeywords(standard.getId(), standard.getGrade(), schoolInfoId, startDate, endDate, keywords, pageable);
                    } else {
                        result = eventRepository.findClassTeacherNotices(standard.getId(), standard.getGrade(), schoolInfoId, startDate, endDate, pageable);
                    }
                } else {
                    if (keywords != null && keywords.size() > 0) {
                        result = eventRepository.findStaffNoticesBySchoolInfoIdAndKeywords(schoolInfoId, startDate, endDate, keywords, pageable);
                    } else {
                        result = eventRepository.findStaffNoticesBySchoolInfoId(schoolInfoId, startDate, endDate, pageable);
                    }
                }
            }
            return result.map(eventMapper::toDto);
        }

        if (keywords != null && keywords.size() > 0) {
            if (standardIds != null && standardIds.size() > 0) {
                result = eventRepository.findAdminNoticesBySchoolInfoIdAndStandardIdsAndKeywords(schoolInfoId, startDate, endDate, keywords, standardIds, pageable);
            } else {
                result = eventRepository.findAdminNoticesBySchoolInfoIdAndKeywords(schoolInfoId, startDate, endDate, keywords, pageable);
            }
        } else {
            if (standardIds != null && standardIds.size() > 0) {
                result = eventRepository.findAdminNoticesBySchoolInfoIdAndStandardIds(schoolInfoId, startDate, endDate, standardIds, pageable);
            } else {
                result = eventRepository.findAdminNoticesBySchoolInfoId(schoolInfoId, startDate, endDate, pageable);
            }
        }

        return result.map(eventMapper::toDto);
    }

    public List<EventDTO> findAllTestAndAssignmentByTeacherInDateRange(Long staffId, LocalDate eventStart, LocalDate eventEnd, ViewType type) throws WitcurveException {
        List<Event> events;
        if(ViewType.ASSIGNMENT.equals(type)){
            events = eventRepository.findAssignmentsByTeacherInDateRange(staffId,eventStart,eventEnd);
        }
        else if(ViewType.TEST.equals(type)){
            events = eventRepository.findTestsByTeacherInDateRange(staffId,eventStart,eventEnd);
        }
        else {
            throw new WitcurveException("Event type should be only TEST and Assignment");
        }
        Collections.sort(events, new EventDateDescComparator());
        return eventMapper.toDto(events);
    }

    @Override
    public List<EventDTO> findHolidaysInSchoolInfo(Long schoolInfoId, LocalDate startDate, LocalDate endDate) throws WitcurveException {
        log.debug("List of holidays for a schoolInfo with id : {}", schoolInfoId);
        WitcurveUtil.correctDateFormat(startDate, endDate);
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
                        throw new WitcurveException("An attendance record for student must have standardId");
                    }
                    StudentStandardDTO studentStandard = studentStandardService.getByStudentId(eventDTO.getStudentId());
                    if(studentStandard == null) {
                        throw new WitcurveException("Student ID: "+eventDTO.getStudentId()+" is not currently mapped to any standard");
                    }
                    if (!studentStandard.getStandard().getId().equals(eventDTO.getStandardId())) {
                        throw new WitcurveException("Student ID: "+eventDTO.getStudentId()+" is not currently mapped with standard ID: " + eventDTO.getStandardId());
                    }
                    schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId();
                    events = eventRepository.eventsBlockingAttendanceForStudent(eventDTO.getDate(), THIRD_LIST, schoolInfoId, eventDTO.getStudentId());
                    events = removeExistingEvent(events, eventDTO);
                } else {
                    schoolInfoId = staffRepository.findById(eventDTO.getStaffId()).get().getSchoolInfo().getId();
                    events = eventRepository.eventsBlockingAttendanceForStaff(eventDTO.getDate(), THIRD_LIST, schoolInfoId, eventDTO.getStaffId());
                    events = removeExistingEvent(events, eventDTO);
                }
                if(events.size() !=0 ) {
                    throw new WitcurveException("Attendance cannot be posted twice, or on a holiday");
                }
            } else if(eventDTO.getType().equals(EventType.NOTICE) || eventDTO.getType().equals(EventType.STAFF_NOTICE)) {
               // if(eventDTO.getSchoolInfoId() == null) {
                 //   throw new WitcurveException("School Info Id is a required field for creating notice");
                //}
                if (eventDTO.getStandardId() != null) {
                    Optional<Standard> standard = standardRepository.findById(eventDTO.getStandardId());
                    if(!standard.isPresent()) {
                        throw new WitcurveException("No standard found with id :" + eventDTO.getStandardId());
                    }
                    if(eventDTO.getSignature() == null) {
                        throw new WitcurveException("Please enter the signature field");
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
