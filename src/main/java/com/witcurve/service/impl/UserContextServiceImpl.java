package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.service.*;
import com.witcurve.service.dto.*;
import com.witcurve.service.mapper.UserMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public UserContextDTO getCurrentUserContext() throws WitcurveException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.getUserWithAuthoritiesByLogin(user.getUsername()).get();

        UserContextDTO contextDTO = new UserContextDTO();
        contextDTO.setCurrentUser(userMapper.userToUserDTO(currentUser));

        Long schoolInfoId = null;
        if (UserType.STAFF.equals(contextDTO.getCurrentUser().getType())) {
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
            contextDTO.setStudentStandardDTO(studentStandardService.getByStudentId(studentDTO.getId()));
            List<CourseTeacherDTO> studentCourses = courseTeacherService.getCourseTeachersByStudentId(studentDTO.getId());
            contextDTO.setStudentCourses(studentCourses);
            if (Strings.isNullOrEmpty(currentUser.getPassword())) {
                contextDTO.getStudentStandardDTO().getStudent().setHasPassword(Boolean.FALSE);
            } else {
                contextDTO.getStudentStandardDTO().getStudent().setHasPassword(Boolean.TRUE);
            }
            schoolInfoId = studentDTO.getSchoolInfo().getId();
        }

        if (schoolInfoId == null) {
            throw new WitcurveException("No schoolInfoId could be found for the current user");
        }

        LocalDate currentDate = LocalDate.now();

        AcademicSessionDTO currentSession = academicSessionService.getCurrentSessionByDate(schoolInfoId, currentDate);
        contextDTO.setCurrentAcademicSession(currentSession);

        if (currentSession != null) {

            contextDTO.setNumberOfCalendarDaysInSession(DAYS.between(currentSession.getStartDate(), currentDate) + 1);
            //TODO: calculate numberOfWorkingDaysInSession

            List<TermDTO> termsInSession = termService.getTermsByAcademicSessionId(currentSession.getId());
            currentSession.setTermsInSession(termsInSession);

            TermDTO currentTerm = null;
            for (TermDTO termDTO : termsInSession) {
                if (!currentDate.isBefore(termDTO.getStartDate())) {
                    currentTerm = termDTO;
                } else {
                    continue;
                }
            }
            if (currentTerm != null) {
                contextDTO.setCurrentTerm(currentTerm);
                contextDTO.setNumberOfCalendarDaysInTerm(DAYS.between(currentTerm.getStartDate(), currentDate) + 1);
                //TODO: calculate numberOfWorkingDaysInTerm

            }

            contextDTO.setNumberOfCalendarDaysInMonth(currentDate.getDayOfMonth());
            //TODO: calculate numberOfWorkingDaysInMonth

        }

        return contextDTO;
    }
}
