package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.SchoolInfo;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.repository.UserRepository;
import com.witcurve.service.*;
import com.witcurve.service.dto.*;
import com.witcurve.service.mapper.InstituteMapper;
import com.witcurve.service.mapper.SchoolInfoMapperLite;
import com.witcurve.service.mapper.SchoolMapperLite;
import com.witcurve.service.mapper.UserMapper;
import com.witcurve.service.util.WeekdayUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@Transactional(readOnly = true)
public class UserContextServiceImpl implements UserContextService {

    private final Logger log  = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CourseTeacherService courseTeacherService;

    @Autowired
    StudentService studentService;

    @Autowired
    StaffService staffService;

    @Autowired
    StudentStandardService studentStandardService;

    @Autowired
    TermService termService;

    @Autowired
    AcademicSessionService academicSessionService;

    @Autowired
    EventService eventService;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    InstituteMapper instituteMapper;

    @Autowired
    SchoolMapperLite schoolMapperLite;

    @Autowired
    SchoolInfoMapperLite schoolInfoMapperLite;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageThreadService messageThreadService;

    @Override
    public UserContextDTO getCurrentUserContext(Long schoolInfoId) throws WitcurveException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.getUserWithAuthoritiesByLogin(user.getUsername()).get();

        UserContextDTO contextDTO = new UserContextDTO();
        contextDTO.setCurrentUser(userMapper.userToUserDTO(currentUser));
        contextDTO.setUnreadCount(messageThreadService.unReadCount(currentUser.getId()));

        if (UserType.TEACHING_STAFF.equals(contextDTO.getCurrentUser().getType())) {
            StaffDTO staffDTO = staffService.getStaffByUserId(currentUser.getId());
            List<CourseTeacherDTO> courseTeachers = courseTeacherService.getCourseTeachersByTeacherId(staffDTO.getId());
            List<StandardDTO> staffStandards = null;
            Map<Long, List<CourseDTO>> standardCourseMap = null;
            for (CourseTeacherDTO courseTeacherDTO : courseTeachers) {
                if (standardCourseMap == null) {
                    staffStandards = new ArrayList<>();
                    standardCourseMap = new HashMap<>();
                }
                if (standardCourseMap.get(courseTeacherDTO.getStandard().getId()) == null) {
                    staffStandards.add(courseTeacherDTO.getStandard());
                    standardCourseMap.put(courseTeacherDTO.getStandard().getId(), new ArrayList<>());
                }
                standardCourseMap.get(courseTeacherDTO.getStandard().getId()).add(courseTeacherDTO.getCourse());
            }
            contextDTO.setStaffStandards(staffStandards);
            contextDTO.setStandardCourseMap(standardCourseMap);
            if (Strings.isNullOrEmpty(currentUser.getPassword())) {
                staffDTO.setHasPassword(Boolean.FALSE);
            } else {
                staffDTO.setHasPassword(Boolean.TRUE);
            }
            contextDTO.getCurrentUser().setStaffDTO(staffDTO);
            schoolInfoId = staffDTO.getSchoolInfo().getId();
        } else if (UserType.PARENT.equals(contextDTO.getCurrentUser().getType())) {
            StudentDTO studentDTO = studentService.getStudentByUserId(currentUser.getId());
            StudentStandardDTO studentStandard = studentStandardService.getByStudentId(studentDTO.getId());
            if (studentStandard != null) {
                contextDTO.setStudentStandardDTO(studentStandardService.getByStudentId(studentDTO.getId()));
                List<CourseTeacherDTO> studentCourses = courseTeacherService.getCourseTeachersByStudentId(studentDTO.getId());
                contextDTO.setStudentCourses(studentCourses);
                if (Strings.isNullOrEmpty(currentUser.getPassword())) {
                    contextDTO.getStudentStandardDTO().getStudent().setHasPassword(Boolean.FALSE);
                } else {
                    contextDTO.getStudentStandardDTO().getStudent().setHasPassword(Boolean.TRUE);
                }
            }

            schoolInfoId = studentDTO.getSchoolInfo().getId();

        } else if (schoolInfoId == null) {
            if (UserType.SUPER_USER.equals(contextDTO.getCurrentUser().getType())) {

                //List<SchoolInfo> allSchoolInfos = schoolInfoRepository.findAll();
                List<SchoolInfo> allSchoolInfos = schoolInfoRepository.findAllForSuperUser();
                setInstituteMapInUserContext(contextDTO, allSchoolInfos);
                return contextDTO;

            } else {
                schoolInfoId = Long.parseLong(user.getUsername().substring(0, user.getUsername().indexOf("-")));
            }
        }

        if (schoolInfoId == null) {
            throw new WitcurveException("No schoolInfoId could be found for the current user");
        } else {
            if (UserType.INSTITUTE_MANAGER.equals(contextDTO.getCurrentUser().getType())) {
                setInstituteMapInUserContext(contextDTO, schoolInfoRepository.findAllForInstituteManager(schoolInfoId));
            } else if (UserType.SCHOOL_MANAGER.equals(contextDTO.getCurrentUser().getType())) {
                setInstituteMapInUserContext(contextDTO, schoolInfoRepository.findAllForSchoolAdmin(schoolInfoId));
            } else {
                setInstituteMapInUserContext(contextDTO, Arrays.asList(schoolInfoRepository.getOne(schoolInfoId)));
            }
        }

        LocalDate currentDate = LocalDate.now();

        AcademicSessionDTO currentSession = academicSessionService.getCurrentSessionByDate(schoolInfoId, currentDate);
        contextDTO.setCurrentAcademicSession(currentSession);

        if (currentSession != null) {

            LocalDate sessionStartDate = currentSession.getStartDate();
            LocalDate sessionEndDate = sessionStartDate.plusYears(1).minusDays(1);
            AcademicSessionDTO nextSession = academicSessionService.getNextSessionSessionAfterDate(schoolInfoId, currentSession.getStartDate());
            if (nextSession != null) {
                sessionEndDate = nextSession.getStartDate().minusDays(1);
            }

            contextDTO.setCurrentSessionStartDate(sessionStartDate);
            contextDTO.setCurrentSessionEndDate(sessionEndDate);

            List<EventDTO> allHolidays = eventService.findHolidaysInSchoolInfo(schoolInfoId, sessionStartDate, sessionEndDate);
            contextDTO.setHolidayList(allHolidays);

            long totalHolidaysInSession = allHolidays.size();
            long totalSundaysInSession = WeekdayUtil.getNoOfWeekDayBetweenDates
                (sessionStartDate, sessionEndDate, DayOfWeek.SUNDAY);
            //TODO: add other holidays missing in this logic

            long noOfHolidaysInSession = allHolidays.size();
            long noOfSundaysInSession =  WeekdayUtil.getNoOfWeekDayBetweenDates
                (sessionStartDate, currentDate, DayOfWeek.SUNDAY);
            for (EventDTO holiday : allHolidays) {
                if (holiday.getDate().isAfter(currentDate)) {
                    noOfHolidaysInSession--;
                }
            }

            //TODO: verify the logic in all the day-count related fields

            contextDTO.setTotalCalendarDaysInSession(DAYS.between(sessionStartDate, sessionEndDate) + 1);
            contextDTO.setTotalWorkingDaysInSession(contextDTO.getTotalCalendarDaysInSession() - totalHolidaysInSession - totalSundaysInSession);

            contextDTO.setNoOfCalendarDaysInSession(DAYS.between(sessionStartDate, currentDate) + 1);
            contextDTO.setNoOfWorkingDaysInSession(contextDTO.getNoOfCalendarDaysInSession() - noOfHolidaysInSession - noOfSundaysInSession);

            List<TermDTO> termsInSession = termService.getTermsByAcademicSessionId(currentSession.getId());
            currentSession.setTermsInSession(termsInSession);

            TermDTO currentTerm = null;
            TermDTO nextTerm = null;
            for (TermDTO termDTO : termsInSession) {
                if (!currentDate.isBefore(termDTO.getStartDate())) {
                    currentTerm = termDTO;
                }
                if (currentDate.isBefore(termDTO.getStartDate())) {
                    nextTerm = termDTO;
                    break;
                }
            }
            if (currentTerm != null) {

                contextDTO.setCurrentTerm(currentTerm);

                LocalDate termStartDate = currentTerm.getStartDate();
                LocalDate termEndDate;

                if (nextTerm != null) {
                    termEndDate = nextTerm.getStartDate().minusDays(1);
                } else {
                    termEndDate = sessionEndDate;
                }


                contextDTO.setCurrentTermStartDate(termStartDate);
                contextDTO.setCurrentTermEndDate(termEndDate);

                //TODO: add holidays missing in this logic
                long totalHolidaysInTerm = 0;
                long noOfHolidaysInTerm = 0;
                long totalSundaysInTerm = WeekdayUtil.getNoOfWeekDayBetweenDates
                    (termStartDate, termEndDate, DayOfWeek.SUNDAY);
                long noOfSundaysInTerm = WeekdayUtil.getNoOfWeekDayBetweenDates
                    (termStartDate, currentDate, DayOfWeek.SUNDAY);
                for (EventDTO holiday : allHolidays) {
                    if (!holiday.getDate().isBefore(termStartDate) && !holiday.getDate().isAfter(termEndDate)) {
                        totalHolidaysInTerm++;
                        if (!holiday.getDate().isAfter(currentDate)) {
                            noOfHolidaysInTerm++;
                        }
                    }
                }
                contextDTO.setTotalCalendarDaysInTerm(DAYS.between(termStartDate, termEndDate) + 1);
                contextDTO.setTotalWorkingDaysInTerm(contextDTO.getTotalCalendarDaysInTerm() - totalHolidaysInTerm - totalSundaysInTerm);

                contextDTO.setNoOfCalendarDaysInTerm(DAYS.between(termStartDate, currentDate) + 1);
                contextDTO.setNoOfWorkingDaysInTerm(contextDTO.getNoOfCalendarDaysInTerm() - noOfHolidaysInTerm - noOfSundaysInTerm);

                LocalDate monthStartDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
                LocalDate monthEndDate = monthStartDate.plusMonths(1).minusDays(1);

                List<EventDTO> monthHolidays = eventService.findHolidaysInSchoolInfo(schoolInfoId, monthStartDate, monthEndDate);

                long totalHolidaysInMonth = monthHolidays.size();
                long totalSundaysInMonth = WeekdayUtil.getNoOfWeekDayBetweenDates
                    (monthStartDate, monthEndDate, DayOfWeek.SUNDAY);
                //TODO: add sundays, and other holidays missing in this logic


                long noOfHolidaysInMonth = monthHolidays.size();
                long noOfSundaysInMonth = WeekdayUtil.getNoOfWeekDayBetweenDates
                    (monthStartDate, currentDate, DayOfWeek.SUNDAY);
                for (EventDTO holiday : monthHolidays) {
                    if (holiday.getDate().isAfter(currentDate)) {
                        noOfHolidaysInMonth--;
                    }
                }

                contextDTO.setTotalCalendarDaysInMonth(DAYS.between(monthStartDate, monthEndDate) + 1);
                contextDTO.setTotalWorkingDaysInMonth(contextDTO.getTotalCalendarDaysInMonth() - totalHolidaysInMonth - totalSundaysInMonth);
                contextDTO.setNoOfCalendarDaysInMonth(currentDate.getDayOfMonth());
                contextDTO.setNoOfWorkingDaysInMonth(contextDTO.getNoOfCalendarDaysInMonth() - noOfHolidaysInMonth - noOfSundaysInMonth);
            }

        }

        return contextDTO;
    }

    private void setInstituteMapInUserContext(UserContextDTO contextDTO, List<SchoolInfo> schoolInfos) {
        for (SchoolInfo schoolInfo : schoolInfos) {
            Long instituteId = schoolInfo.getSchool().getInstitute().getId();
            Long schoolId = schoolInfo.getSchool().getId();

            if (contextDTO.getInstituteMap() == null) {
                contextDTO.setInstituteMap(new LinkedHashMap<>());
            }
            if (contextDTO.getInstituteMap().get(instituteId) == null) {
                contextDTO.getInstituteMap().put(instituteId, instituteMapper.toDto(schoolInfo.getSchool().getInstitute()));
            }
            if (contextDTO.getInstituteMap().get(instituteId).getSchoolMap() == null) {
                contextDTO.getInstituteMap().get(instituteId).setSchoolMap(new LinkedHashMap<>());
            }
            if (contextDTO.getInstituteMap().get(instituteId).getSchoolMap().get(schoolId) == null) {
                contextDTO.getInstituteMap().get(instituteId).getSchoolMap().put(schoolId, schoolMapperLite.toDto(schoolInfo.getSchool()));
            }
            SchoolInfoDTO schoolInfoDTO = schoolInfoMapperLite.toDto(schoolInfo);
            List<User> users = userRepository.findSchoolManagerBySchoolInfoId(schoolInfo.getId());
            if(users != null && users.size() != 0) {
                schoolInfoDTO.setMainSchoolInfoUserId(users.get(0).getId());
            }
            contextDTO.getInstituteMap().get(instituteId).getSchoolMap().get(schoolId).addSchoolInfo(schoolInfoDTO);
        }
    }
}
