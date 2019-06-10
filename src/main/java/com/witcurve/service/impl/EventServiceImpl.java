package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.config.Constants;
import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.*;
import com.witcurve.repository.*;
import com.witcurve.service.*;
import com.witcurve.service.dto.AcademicSessionDTO;
import com.witcurve.service.dto.EventDTO;
import com.witcurve.service.dto.PeriodicTestDTO;
import com.witcurve.service.dto.StudentStandardDTO;
import com.witcurve.service.mapper.EventMapper;
import com.witcurve.service.util.LocalDateComparator;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
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

    @Autowired
    EventContentRepository eventContentRepository;

    @Autowired
    StudentMarksRepository studentMarksRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    MailService mailService;

    @Autowired
    AcademicSessionService academicSessionService;

    @Autowired
    SmsService smsService;

    private static final ArrayList<EventType> FIRST_LIST = new ArrayList<>(
        Arrays.asList(EventType.ASSIGNMENT, EventType.DAILY_UPDATE, EventType.TEST, EventType.PERIODIC_TEST));

    private static final ArrayList<EventType> SECOND_LIST = new ArrayList<>(
        Arrays.asList(EventType.HOLIDAY, EventType.SCHOOL_EVENT));

    private static final ArrayList<EventType> THIRD_LIST = new ArrayList<>(
        Arrays.asList(EventType.HOLIDAY, EventType.ATTENDANCE));

    private static final ArrayList<String> LIST_FOR_DAY = new ArrayList<>(
        Arrays.asList(EventType.DAILY_UPDATE.toString(), EventType.TEST.toString()));

    private static final ArrayList<String> LIST_FOR_DATE_RANGE = new ArrayList<>(
        Arrays.asList(EventType.TEST.toString()));


    @Override
    public List<EventDTO> saveOrUpdate(List<EventDTO> eventDTOs, Long schoolInfoId) throws WitcurveException, UnsupportedEncodingException {
        log.debug("Request to save or update eventDTOs : {}", eventDTOs);
        isEventValid(eventDTOs);
        Optional<SchoolInfo> result = null;

        if (schoolInfoId != null) {//TODO: remove 'if' condition after making schoolInfoId mandatory
            result = schoolInfoRepository.findById(schoolInfoId);
            if (!result.isPresent()) {
                throw new WitcurveException("No school info found with ID: " + schoolInfoId);
            }
        }
        String bindingId = UUID.randomUUID().toString();
        for(EventDTO eventDTO : eventDTOs) {
            if(eventDTO.getKeywords()!= null) {
                for (String keyword : eventDTO.getKeywords()) {
                    keywordRepository.save(new Keyword(keyword));
                }
            }
            if(eventDTO.getType().equals(EventType.PERIODIC_TEST)) {
                eventDTO.setBindingId(bindingId);
            }
        }

        List<Event> events = eventMapper.toEntity(eventDTOs);

        Map<Long, Set<LocalDate>> studentIdAndDatesMap = null;
        Map<Long, Set<LocalDate>> staffIdAndDatesMap = null;

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
                    if (event.getId() == null && event.getAttendanceType().equals(AttendanceType.ABSENT)) {
                        if (studentIdAndDatesMap == null) {
                            studentIdAndDatesMap = new HashMap<>();
                        }
                        if (studentIdAndDatesMap.get(event.getStudent().getId()) == null) {
                            studentIdAndDatesMap.put(event.getStudent().getId(), new HashSet<>());
                        }
                        studentIdAndDatesMap.get(event.getStudent().getId()).add(event.getDate());
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
                    if (event.getId() == null && event.getAttendanceType().equals(AttendanceType.ABSENT)) {
                        if (staffIdAndDatesMap == null) {
                            staffIdAndDatesMap = new HashMap<>();
                        }
                        if (staffIdAndDatesMap.get(event.getStaff().getId()) == null) {
                            staffIdAndDatesMap.put(event.getStaff().getId(), new HashSet<>());
                        }
                        staffIdAndDatesMap.get(event.getStaff().getId()).add(event.getDate());
                    }
                }
            }
        }

        if (result != null && result.isPresent()) { //TODO: remove 'if' condition once schoolInfoId is made mandatory

            if (studentIdAndDatesMap != null || staffIdAndDatesMap != null) {

                SchoolInfo schoolInfo = result.get();
                String instituteName = schoolInfo.getSchool().getInstitute().getName();
                String smsSignature = schoolInfo.getSchool().getInstitute().getSmsSignature();

                Map paramsMap = new HashMap();
                paramsMap.put(Constants.PARAM_INSTITUTE_NAME, instituteName);

                if (staffIdAndDatesMap != null) {
                    String[] subjectParamArray = new String[]{smsSignature, ""};
                    Map<Long, Staff> staffIdMap = new HashMap<>();
                    List<Staff> staffList = staffRepository.findAllById(staffIdAndDatesMap.keySet());
                    for (Staff staff: staffList) {
                        if (staffIdMap == null) {
                            staffIdMap = new HashMap<>();
                        }
                        staffIdMap.put(staff.getId(), staff);
                    }
                    String smsBody = "This is to notify you that your attendance is marked absent on %s. Visit %s for more.";
                    for (Long staffId: staffIdAndDatesMap.keySet()) {
                        Staff staff = staffIdMap.get(staffId);
                        String fullName = staff.getFirstName() + " " + staff.getLastName();
                        paramsMap.put(Constants.PARAM_FULL_NAME, fullName);
                        //TODO: add logic for tiny Urls
                        String tinyUrl = "http://witcurve.com/tiny";
                        paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                        for (LocalDate date: staffIdAndDatesMap.get(staffId)) {
                            paramsMap.put(Constants.PARAM_DATE, date);
                            subjectParamArray[1] = date.toString();
                            paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                            mailService.sendEmailFromTemplate(staff.getEmail(), paramsMap, "mail/notification/staffAbsentNotificationEmail", "email.notification.staff.absent.title", smsSignature);
                            smsService.sendSms(staff.getPrimaryPhone(), String.format(smsBody, date, tinyUrl), smsSignature);
                        }
                    }
                }

                if (studentIdAndDatesMap != null) {
                    String[] subjectParamArray = new String[]{smsSignature, "", ""};
                    Map<Long, Student> studentIdMap = new HashMap<>();
                    List<Student> studentList = studentRepository.findAllById(studentIdAndDatesMap.keySet());
                    for (Student student: studentList) {
                        studentIdMap.put(student.getId(), student);
                    }
                    String smsBody = "This is to notify you that your ward %s is absent on %s. Visit %s for more.";
                    for (Long studentId: studentIdAndDatesMap.keySet()) {
                        Student student = studentIdMap.get(studentId);
                        if (Strings.isNullOrEmpty(student.getEmail())) {
                            continue;
                        }
                        String fullName = student.getFirstName() + " " + student.getLastName();
                        paramsMap.put(Constants.PARAM_FULL_NAME, fullName);
                        //TODO: add logic for tiny Urls
                        String tinyUrl = "http://witcurve.com/tiny";
                        paramsMap.put(Constants.PARAM_TINY_URL, tinyUrl);
                        subjectParamArray[1] = fullName;
                        for (LocalDate date: studentIdAndDatesMap.get(studentId)) {
                            paramsMap.put(Constants.PARAM_DATE, date);
                            subjectParamArray[2] = date.toString();
                            paramsMap.put(Constants.PARAM_MAIL_SUBJECT, subjectParamArray);
                            mailService.sendEmailFromTemplate(student.getEmail(), paramsMap, "mail/notification/studentAbsentNotificationEmail", "email.notification.student.absent.title", smsSignature);
                            smsService.sendSms(student.getRegisteredMobileNumber(), String.format(smsBody, fullName, date, tinyUrl), smsSignature);
                        }
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
        Optional<Event> event = eventRepository.findById(eventId);
        if (!event.isPresent()) {
            throw new WitcurveException("No Event with given id");
        }
        return eventMapper.toDto(event.get());
    }

    @Override
    public void deleteEvent(Long eventId) throws WitcurveException {
        log.debug("Request to delete event with id : {}", eventId);
        Event event = eventRepository.findById(eventId).get();

        if (event == null){
            throw new WitcurveException("No Event with given id");
        }
        if (event.getType() == EventType.TEST
            || event.getType() == EventType.ASSIGNMENT
            || event.getType() == EventType.DAILY_UPDATE) {
            eventContentRepository.deleteByEventId(event.getId());
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
            Long schoolInfoId = studentStandard.getStandard().getSchoolInfo().getId() ;

            AcademicSessionDTO currentSession = academicSessionService.getCurrentSessionByDate(schoolInfoId, LocalDate.now());
            List<BigInteger> eventIds =  new ArrayList<>();
            long substractDays = 6;
            boolean breakCycle = false;
            while(eventIds.size() < 20) {
                if(eventDate.minusDays(substractDays).isBefore(currentSession.getStartDate())) {
                    substractDays = Period.between(eventDate.minusDays(substractDays), currentSession.getStartDate()).getDays();
                    breakCycle = true;
                }
                eventIds = eventRepository.findDirayEventsByDateRangeForStudent(eventDate.minusDays(6), eventDate, standardId, Arrays.asList(EventType.DAILY_UPDATE.toString()));
                if(breakCycle) {
                    break;
                }
                substractDays += 7;
            }
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
            Set<Long> courseIds = new HashSet<>();
            for(CourseTeacher courseTeacher : courseTeachers) {
                standardIds.add(courseTeacher.getStandard().getId());
                grades.add(courseTeacher.getStandard().getGrade().toString());
                courseTeacherIds.add(courseTeacher.getId());
                courseIds.add(courseTeacher.getCourse().getId());
            }
            Optional<Staff> staff = staffRepository.findById(staffId);
            if (!staff.isPresent()) {
                throw new WitcurveException("No staff found with ID: " + staffId);
            }
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(eventDate, eventDate, staffId, courseTeacherIds, courseIds, standardIds, grades, staff.get().getSchoolInfo().getId(), LIST_FOR_DAY);
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

            Set<Long> courseIds = courseTeachers
                .stream()
                .map(CourseTeacher::getCourse)
                .map(s -> s.getId())
                .collect(Collectors.toSet());

            LocalDate monthStart = LocalDate.of(year,month,1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(monthStart, monthEnd, staffId, courseTeacherIds, courseIds, standardIds, grades, schoolInfo.getId(), LIST_FOR_DATE_RANGE);
            result = eventRepository.findAllById(convertBigIntToLong(eventIds));
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
    public List<EventDTO> findHolidaysOrSchoolEventsBySchoolInfoId(LocalDate fromDate, LocalDate endDate, Long schoolInfoId) throws WitcurveException {
        log.debug("Find events for holidays and school events between from date : {} to end date : {} for school info with id : {}", fromDate, endDate, schoolInfoId);
        WitcurveUtil.correctDateFormat(fromDate, endDate);
        List<Event> result = eventRepository.findHolidaysAndSchoolEventsBetweenFromDateAndToDate(fromDate, endDate, schoolInfoId);
        Collections.sort(result, new EventDateAscComparator());
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

            Set<Long> courseIds = courseTeachers
                .stream()
                .map(CourseTeacher::getCourse)
                .map(s -> s.getId())
                .collect(Collectors.toSet());

            List<BigInteger> eventIds = eventRepository.findEventsByDateRangeForStaff(date, endDate, staffId, courseTeacherIds, courseIds, standardIds, grades, schoolInfo.getId(), LIST_FOR_DATE_RANGE);
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

    @Override
    public Page<PeriodicTestDTO> getPeriodTestsBetweenDates(Pageable pageable, LocalDate startDate, LocalDate endDate, Long schoolInfoId, List<Grade> grades) throws WitcurveException {
        List<Event> events = null;
        if(grades != null && !grades.isEmpty()) {
            List<String> bindingIds = eventRepository.findPeriodicTestBindingIdBySchoolInfoIdAndGrades(schoolInfoId, grades, startDate, endDate);
            if(bindingIds!= null && !bindingIds.isEmpty()) {
                events = eventRepository.findPeriodicTestByBindingIds(bindingIds);
            } else {
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        } else {
            events = eventRepository.findPeriodicTestsBySchoolInfoId(schoolInfoId, startDate, endDate);
        }
        List<PeriodicTestDTO> periodicTestDTOList = new ArrayList<>();
        Map<String, PeriodicTestDTO> periodicTestMap= new HashMap<>();
        if(events.isEmpty()) {
            return new PageImpl<>(periodicTestDTOList, pageable, 0);
        } else {
            for (Event event : events) {
                PeriodicTestDTO periodicTestDTO = periodicTestMap.get(event.getBindingId());
                if(periodicTestDTO == null) {
                    periodicTestDTO = new PeriodicTestDTO();
                    periodicTestDTO.setCreatedBy(event.getCreatedBy());
                    periodicTestDTO.setCreatedDate(event.getCreatedDate());
                    periodicTestDTO.setLastModifiedBy(event.getLastModifiedBy());
                    periodicTestDTO.setLastModifiedDate(event.getLastModifiedDate());
                    periodicTestDTO.setBindingId(event.getBindingId());
                    periodicTestDTO.setName(event.getName());
                    periodicTestDTO.setDescription(event.getDescription());
                    periodicTestDTO.setDateList(Arrays.asList(event.getDate()));
                    periodicTestDTO.setGrades(Arrays.asList(event.getGrade()));
                } else {
                    if(event.getCreatedDate().isBefore(periodicTestDTO.getCreatedDate())) {
                        periodicTestDTO.setCreatedDate(event.getCreatedDate());
                    }
                    List<LocalDate> dates = new ArrayList<>(periodicTestDTO.getDateList());
                    if(!dates.contains(event.getDate())) {
                        dates.add(event.getDate());
                        periodicTestDTO.setDateList(dates);
                    }
                    List<Grade> gradeList = new ArrayList<>(periodicTestDTO.getGrades());
                    if(!gradeList.contains(event.getGrade())) {
                        gradeList.add(event.getGrade());
                        periodicTestDTO.setGrades(gradeList);
                    }
                }
                periodicTestMap.put(event.getBindingId(), periodicTestDTO);
            }
            periodicTestDTOList = new ArrayList<>(periodicTestMap.values());
            for(PeriodicTestDTO periodicTestDTO : periodicTestDTOList) {
                List<LocalDate> dates = periodicTestDTO.getDateList();
                Collections.sort(dates, new LocalDateComparator());
                periodicTestDTO.setDateList(dates);
                SortedSet<Grade> gradeSet = new TreeSet<>();
                gradeSet.addAll(periodicTestDTO.getGrades());
                periodicTestDTO.setGrades(new ArrayList<>(gradeSet));
            }
            Collections.sort(periodicTestDTOList, new PeriodicTestDescComparator());
            List<PeriodicTestDTO> result = new ArrayList<>();
            int startIndex = pageable.getPageNumber()*pageable.getPageSize();
            int endIndex = startIndex + pageable.getPageSize()-1;
            for(int i=startIndex; i<=endIndex; i++ ) {
                if(i>periodicTestDTOList.size()-1) {
                    break;
                }
                result.add(periodicTestDTOList.get(i));
            }
            return new PageImpl<>(result, pageable, periodicTestDTOList.size());
        }
    }

    @Override
    public List<EventDTO> getPeriodicTestsByBindingId(String bindingId, List<Long> courseIds) {
        log.debug("Get list of periodic tests by binding Id : {} and courseIds : {}", bindingId, courseIds);
        if(courseIds!= null && !courseIds.isEmpty()) {
            return eventMapper.toDto(eventRepository.findPeriodicEventsByBindingIdAndCourseIds(bindingId, courseIds));
        } else {
            return eventMapper.toDto(eventRepository.findPeriodicEventsByBindingId(bindingId));
        }
    }

    @Override
    public void deletePeriodicTestsByBindingId(String bindingId) throws WitcurveException {
        log.debug("Delete list of periodic tests by binding Id : {}", bindingId);
        List<Long> eventIds = eventRepository.findPeriodicEventIdsByBindingId(bindingId);
        List<StudentMarks> studentMarks = studentMarksRepository.getStudentMarksByEventId(eventIds);
        if(!studentMarks.isEmpty()) {
            throw new WitcurveException("This periodic test cannot be deleted as marks has already been entered");
        }
        eventContentRepository.deleteByEventId(eventIds);
        eventRepository.deletePeriodicEventByBindingId(bindingId);

    }

    @Override
    public List<EventDTO> findAllTestAndAssignmentByTeacherInDateRange(Long staffId, LocalDate eventStart, LocalDate eventEnd, ViewType type) throws WitcurveException {
        List<Event> events;
        if(ViewType.ASSIGNMENT.equals(type)){
            events = eventRepository.findAssignmentsByTeacherInDateRange(staffId,eventStart,eventEnd);
        }
        else if(ViewType.TEST.equals(type)){
            events = eventRepository.findTestsByTeacherInDateRange(staffId,eventStart,eventEnd);
        } else if(ViewType.PERIODIC_TEST.equals(type)) {
            List<CourseTeacher> courseTeachers = courseTeacherRepository.findByTeacherId(staffId);
            Set<Long> courseIds = courseTeachers
                .stream()
                .map(CourseTeacher::getCourse)
                .map(s -> s.getId())
                .collect(Collectors.toSet());
            events = eventRepository.findPeriodicTestsByTeacherInDateRange(courseIds,eventStart,eventEnd);
        }
        else {
            throw new WitcurveException("Event type should be TEST, ASSIGNMENT OR PERIODIC TEST");
        }
        Collections.sort(events, new EventDateDescComparator());
        return eventMapper.toDto(events);
    }

    @Override
    public Page<EventDTO> findAllTestAndAssignmentAndDailyUpdateByStandardAndCourse(Pageable pageable, LocalDate eventStart, LocalDate eventEnd, ViewType type, Long standardId, Long courseId) throws WitcurveException {
        WitcurveUtil.correctDateFormat(eventStart, eventEnd);
        List<Event> events = null;
        List<Long> courseTeacherIds = courseTeacherRepository.findActiveCourseTeachersByStandardIdAndCourseId(standardId, courseId);
        if(courseTeacherIds.size() ==0) {
            throw new WitcurveException("There are no teacher assigned to this for course in given standard");
        }
        if(type.equals(ViewType.ASSIGNMENT)) {
            events = eventRepository.findAssignmentsByCourseTeachersInDateRange(courseTeacherIds, eventStart, eventEnd);
        } else if(type.equals(ViewType.TEST)) {
            events = eventRepository.findTestsByCourseTeachersInDateRange(courseTeacherIds, eventStart, eventEnd);
        } else if (type.equals(ViewType.DAILY_UPDATE)) {
            events = eventRepository.findDailyUpdatesByCourseTeachersInDateRange(courseTeacherIds, eventStart, eventEnd);
        } else if (type.equals(ViewType.PERIODIC_TEST)) {
            events = eventRepository.findPeriodicTestsByCourseTeachersInDateRange(courseId, eventStart, eventEnd);
        } else {
            throw new WitcurveException("Invalid Event Type");
        }
        events = new ArrayList<>(events);
        Collections.sort(events, new EventDateDescComparator());
        List<Event> finalList = new ArrayList<>();
        int startIndex = pageable.getPageNumber()*pageable.getPageSize();
        int endIndex = startIndex + pageable.getPageSize()-1;
        for(int i=startIndex; i<=endIndex; i++ ) {
            if(i>events.size()-1) {
                break;
            }
            finalList.add(events.get(i));
        }
        List<EventDTO> result =  eventMapper.toDto(finalList);
        return new PageImpl<>(new ArrayList<>(result), pageable, events.size());
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
                throw new WitcurveException("All event requests except ATTENDANCE type require scd or  course teacher id");
            }
            if(FIRST_LIST.contains(eventDTO.getType())) {
                if(eventDTO.getType().equals(EventType.ASSIGNMENT)) {
                    if(eventDTO.getStandardId() == null) {
                        log.error("Event of type : "+eventDTO.getType()+"cannot have empty standardId");
                        throw new WitcurveException("An assignment cannot have empty standardId for event with date");
                    }
                    Event event = eventRepository.findAssignmentOnDateAndCourseTeacher(eventDTO.getDate(),eventDTO.getCourseTeacher().getId());
                    if(event != null && !event.getId().equals(eventDTO.getId())) {
                        throw new WitcurveException("There already exists a record for given event type and date for course teacher with with id: " +eventDTO.getScd().getId());
                    }

                } else if(eventDTO.getType().equals(EventType.PERIODIC_TEST)) {
                    if(eventDTO.getGrade() == null || eventDTO.getSchoolInfoId() == null) {
                        log.error("Event of type : "+eventDTO.getType()+"cannot have empty grade or school info id");
                        throw new WitcurveException("A periodic test creation need both grade id and school info id");
                    }
                    Optional<CourseTeacher> courseTeacher = courseTeacherRepository.findById(eventDTO.getCourseTeacher().getId());
                    if (!courseTeacher.isPresent()) {
                        throw new WitcurveException("No course teacher with given id " + eventDTO.getCourseTeacher().getId());
                    }
                    Event event = eventRepository.findPeriodicTestOnDateAndCourseId(eventDTO.getDate(), courseTeacher.get().getCourse().getId());
                    if(event != null && !event.getId().equals(eventDTO.getId())) {
                        throw new WitcurveException("There already exists a record for given event type and date for course with with id: " +courseTeacher.get().getCourse().getId());
                    }
                }
                else{
                    if(eventDTO.getStandardId() == null || eventDTO.getScd() == null || (eventDTO.getScd() != null && eventDTO.getScd().getId() == null)) {
                        throw new WitcurveException("Event cannot have SCD without standardId");
                    }
//                    List<SlotCourseDetailsDTO> slotCourseDetails = slotCourseDetailsService.getSlotCourseDetailsByStandardId(eventDTO.getStandardId());
//                    if(!slotCourseDetails.contains(eventDTO.getScd())) {
//                        throw new WitcurveException("This scd doesn't belong to given standard id");
//                    }
                    Event event = eventRepository.findTestOnDateAndSlot(eventDTO.getDate(), eventDTO.getScd().getId());
                    if(event != null && !event.getId().equals(eventDTO.getId())) {
                        throw new WitcurveException("There already exists a record for given event type and date for SCD with with id: " +eventDTO.getScd().getId());
                    }
                }
            } else if(eventDTO.getType().equals(EventType.HOLIDAY)) {
                if(eventDTO.getSchoolInfoId() == null) {
                    throw new WitcurveException("A holiday must have schoolInfoId");
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
            }  else if(eventDTO.getType().equals(EventType.ATTENDANCE)) {
                if(!(eventDTO.getStudentId() == null ^ eventDTO.getStaffId() == null)) {
                    log.error("Event of type : "+eventDTO.getType()+"should have only one of the fields : studentId, staffId");
                    throw new WitcurveException("An attendance record must have only one of the fields [studentId, staffId]");
                }
                Long schoolInfoId;
                List<Event> events;
                Boolean forStudent = true;
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
                    forStudent = false;
                    schoolInfoId = staffRepository.findById(eventDTO.getStaffId()).get().getSchoolInfo().getId();
                    events = eventRepository.eventsBlockingAttendanceForStaff(eventDTO.getDate(), THIRD_LIST, schoolInfoId, eventDTO.getStaffId());
                    events = removeExistingEvent(events, eventDTO);
                }
                if(events.size() !=0 ) {
                    throw new WitcurveException(String.format("Either attendance already taken on this day for given %s or this is a holiday", forStudent? "student": "staff"));
                }
            } else if(eventDTO.getType().equals(EventType.NOTICE) || eventDTO.getType().equals(EventType.STAFF_NOTICE)) {
                if(eventDTO.getSchoolInfoId() == null) {
                    throw new WitcurveException("School Info Id is a required field for creating notice");
                }
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

    public class PeriodicTestDescComparator implements Comparator<PeriodicTestDTO> {

        @Override
        public int compare(PeriodicTestDTO o1, PeriodicTestDTO o2) {
            if (o1.getCreatedDate().isAfter( o2.getCreatedDate())) {
                return -1;
            } else if(o1.getCreatedDate().isBefore( o2.getCreatedDate())) {
                return 1;
            } else {
                return 0;
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
