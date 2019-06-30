package com.witcurve.service.impl;

import com.witcurve.domain.enumeration.ApprovalStatus;
import com.witcurve.domain.enumeration.AttendanceType;
import com.witcurve.domain.enumeration.MessageType;
import com.witcurve.service.*;
import com.witcurve.service.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService {

    @Autowired
    private StaffService staffService;

    @Autowired
    private StandardService standardService;

    @Autowired
    private AcademicSessionService academicSessionService;

    @Autowired
    private EventService eventService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private MessageThreadService messageThreadService;

    @Autowired
    private StudentStandardService studentStandardService;

    /***
     * get content for home screen for admin
     * @param userId
     * @param schoolInfoId
     * @return
     */
    @Override
    public HomeDTO getCurrentUserHomeContentForAdmin(Long userId, Long schoolInfoId) {
        LocalDate today = LocalDate.now();
        HomeDTO homeDTO = new HomeDTO();

        // staff and student for whole school
        Map<String, Map<AttendanceType, Long>> attendanceMap = new HashMap<>();
        attendanceMap.put("student", getAttendanceMap(eventService.getAttendance(today, today, null, null, null, schoolInfoId, Boolean.TRUE)));
        attendanceMap.put("staff", getAttendanceMap(eventService.getAttendance(today, today, null, null, null, schoolInfoId, Boolean.FALSE)));
        homeDTO.setAttendanceMap(attendanceMap);

        //staff leave applications
        homeDTO.setLeaveApplications(getLeaveApplications(schoolInfoId, today, null, Boolean.FALSE));

        //meeting request for user
        homeDTO.getInbox().put("meetings", getPendingMeetingRequest(userId));

        //message from parent and staff
        List<MessageThreadDTO> messageThreadDTOS = getUnreadBoardAdminMessage(userId);
        homeDTO.getInbox().put("messageFromParent", messageThreadDTOS.stream()
            .filter(messageThreadDTO -> messageThreadDTO.getUserType().equals("Parent")).collect(Collectors.toList()));
        homeDTO.getInbox().put("messageFromStaff", messageThreadDTOS
            .stream().filter(messageThreadDTO -> messageThreadDTO.getUserType().equals("Teacher")).collect(Collectors.toList()));

        //upcoming events
        homeDTO.setUpcomingEvents(eventService.findHolidaysOrSchoolEventsBySchoolInfoId(today, today.plusWeeks(1), schoolInfoId));

        //birthdays for student and staff
        homeDTO.setBirthdayStudents(studentStandardService.getBySchoolInfoId(schoolInfoId, null ,null).stream()
            .filter(ss -> isBirthdayToday(ss.getStudent().getDateOfBirth())).collect(Collectors.toList()));
        homeDTO.setBirthdayStaff(staffService.getStaffBySchoolInfoId(schoolInfoId, Boolean.FALSE).stream()
            .filter(staffDTO -> isBirthdayToday(staffDTO.getDateOfBirth())).collect(Collectors.toList()));

        return homeDTO;
    }

    /***
     * get content for home screen for staff
     * @param userId
     * @param schoolInfoId
     * @return
     */
    @Override
    public HomeDTO getCurrentUserHomeContentForStaff(Long userId, Long schoolInfoId) {
        LocalDate today = LocalDate.now();
        HomeDTO homeDTO = new HomeDTO();

        StaffDTO staffDTO = staffService.getStaffByUserId(userId);
        List<StandardDTO> standardDTOS = standardService.getStandardsByTeacherId(staffDTO.getId());

        if (!CollectionUtils.isEmpty(standardDTOS)) {
            //attendance and birthday student for all standards
            homeDTO.setAttendanceMap(getAttendanceMapForStaff(today, standardDTOS));
            homeDTO.setBirthdayStudents(studentStandardService.getByStaffId(staffDTO.getId(), null, null).stream()
                .filter(ss -> isBirthdayToday(ss.getStudent().getDateOfBirth())).collect(Collectors.toList()));

            // leave applications and attendance for for class teacher
            StandardDTO classStandard = standardDTOS.stream().filter(standardDTO -> staffDTO.getId().equals(standardDTO.getClassTeacherId())).findFirst().orElse(null);
            if (classStandard != null) {
                homeDTO.setLeaveApplications(getLeaveApplications(schoolInfoId, today, classStandard, Boolean.TRUE));
                homeDTO.setClassAttendance(homeDTO.getAttendanceMap().remove(classStandard.getGrade() + " " + classStandard.getSection()));
            }
        }

        // meetings and messages
        homeDTO.getInbox().put("meetings", getPendingMeetingRequest(userId));
        homeDTO.getInbox().put("messageFromParent", getUnreadMessage(userId));
        homeDTO.getInbox().put("messageFromAdmin", getUnreadBoardAdminMessage(userId));

        //upcoming events
        homeDTO.setUpcomingEvents(eventService.findUpcomingEventsForStaffInWeek(today, staffDTO.getId()));

        return homeDTO;
    }

    /***
     * get attendance map for given standard and date
     * @param date
     * @param standardDTOS
     * @return
     */
    private Map<String, Map<AttendanceType, Long>> getAttendanceMapForStaff(LocalDate date, List<StandardDTO> standardDTOS) {
        Map<String, Map<AttendanceType, Long>> attendanceMap = new HashMap<>();

        for (StandardDTO standardDTO : standardDTOS) {
            List<EventDTO> eventDTOS = eventService.getAttendance(date, date, null, standardDTO.getId(), null, null, null);
            attendanceMap.put(standardDTO.getGrade() + " " + standardDTO.getSection(), getAttendanceMap(eventDTOS));
        }

        return attendanceMap;
    }

    /***
     * get attendance count map from event list
     * @param eventDTOS
     * @return
     */
    private Map<AttendanceType, Long> getAttendanceMap(List<EventDTO> eventDTOS) {
        if (!CollectionUtils.isEmpty(eventDTOS)) {
            Map<AttendanceType, Long> map = new HashMap<>();

            map.put(AttendanceType.PRESENT, eventDTOS.stream().filter(event -> event.getAttendanceType().equals(AttendanceType.PRESENT)).count());
            map.put(AttendanceType.ABSENT, eventDTOS.stream().filter(event -> event.getAttendanceType().equals(AttendanceType.ABSENT)).count());
            map.put(AttendanceType.HALF_DAY, eventDTOS.stream().filter(event -> event.getAttendanceType().equals(AttendanceType.HALF_DAY)).count());
            map.put(AttendanceType.GRACE, eventDTOS.stream().filter(event -> event.getAttendanceType().equals(AttendanceType.GRACE)).count());
            map.put(AttendanceType.LATE, eventDTOS.stream().filter(event -> event.getAttendanceType().equals(AttendanceType.LATE)).count());

            return map;
        }
        return null;
    }

    /***
     * get pending leave application from staff or student
     * @param schoolInfoId
     * @param date
     * @param standardDTO
     * @param forStaff
     * @return
     */
    private List<LeaveApplicationDTO> getLeaveApplications(Long schoolInfoId, LocalDate date, StandardDTO standardDTO, Boolean forStaff) {
        AcademicSessionDTO currentSession = academicSessionService.getCurrentSessionByDate(schoolInfoId, date);


        List<LeaveApplicationDTO> leaveApplicationDTOS = new ArrayList<>();
        Map<Long, List<LeaveApplicationDTO>> leaveApplicationMap;

        if (forStaff) {
            leaveApplicationMap = leaveApplicationService
                .getLeaveDetails(null, null, standardDTO.getId(), null, currentSession.getStartDate(),
                    currentSession.getStartDate().plusYears(1).minusDays(1), ApprovalStatus.PENDING);
        } else {
            leaveApplicationMap = leaveApplicationService
                .getLeaveDetails(null, null, null, schoolInfoId, currentSession.getStartDate(),
                    currentSession.getStartDate().plusYears(1).minusDays(1), ApprovalStatus.PENDING);
        }


        if (!CollectionUtils.isEmpty(leaveApplicationMap)) {
            leaveApplicationMap.values().forEach(leaveApplicationDTOS::addAll);
        }
        return leaveApplicationDTOS;
    }

    /***
     * get unread meeting request
     * @param userId
     * @return
     */
    private List<MessageThreadDTO> getPendingMeetingRequest(Long userId) {
        return messageThreadService.getInboxMessageThreadsByUserId(Pageable.unpaged(), false, false,
            userId, MessageType.MEETING_REQUEST, ApprovalStatus.PENDING, null).getContent();
    }

    /***
     * get unread personal message
     * @param userId
     * @return
     */
    private List<MessageThreadDTO> getUnreadMessage(Long userId) {
        return messageThreadService.getInboxMessageThreadsByUserId(Pageable.unpaged(), false, false,
            userId, MessageType.PERSONAL, null, false).getContent();
    }

    /***
     * get unread admin message
     * @param userId
     * @return
     */
    private List<MessageThreadDTO> getUnreadBoardAdminMessage(Long userId) {
        return messageThreadService.getInboxMessageThreadsByUserId(Pageable.unpaged(), false, true,
            userId, MessageType.PERSONAL, null, false).getContent();
    }

    /***
     * returns true if given dob is birthday
     * @param dob
     * @return
     */
    private Boolean isBirthdayToday(LocalDate dob) {
        return MonthDay.now().equals(MonthDay.of(dob.getMonth(), dob.getDayOfMonth()));
    }
}
