package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.witcurve.domain.enumeration.AttendanceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeDTO {
    private Map<String, Map<AttendanceType, Long>> attendanceMap = new HashMap<>();

    private Map<AttendanceType, Long> classAttendance;

    private List<LeaveApplicationDTO> leaveApplications;

    private Map<String, List<MessageThreadDTO>> inbox = new HashMap<>();

    private List<EventDTO> upcomingEvents;

    private List<StudentStandardDTO> birthdayStudents;

    private List<StaffDTO> birthdayStaff;

    public Map<String, Map<AttendanceType, Long>> getAttendanceMap() {
        return attendanceMap;
    }

    public void setAttendanceMap(Map<String, Map<AttendanceType, Long>> attendanceMap) {
        this.attendanceMap = attendanceMap;
    }

    public Map<AttendanceType, Long> getClassAttendance() {
        return classAttendance;
    }

    public void setClassAttendance(Map<AttendanceType, Long> classAttendance) {
        this.classAttendance = classAttendance;
    }

    public List<LeaveApplicationDTO> getLeaveApplications() {
        return leaveApplications;
    }

    public void setLeaveApplications(List<LeaveApplicationDTO> leaveApplications) {
        this.leaveApplications = leaveApplications;
    }

    public Map<String, List<MessageThreadDTO>> getInbox() {
        return inbox;
    }

    public void setInbox(Map<String, List<MessageThreadDTO>> inbox) {
        this.inbox = inbox;
    }

    public List<EventDTO> getUpcomingEvents() {
        return upcomingEvents;
    }

    public void setUpcomingEvents(List<EventDTO> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }

    public List<StudentStandardDTO> getBirthdayStudents() {
        return birthdayStudents;
    }

    public void setBirthdayStudents(List<StudentStandardDTO> birthdayStudents) {
        this.birthdayStudents = birthdayStudents;
    }

    public List<StaffDTO> getBirthdayStaff() {
        return birthdayStaff;
    }

    public void setBirthdayStaff(List<StaffDTO> birthdayStaff) {
        this.birthdayStaff = birthdayStaff;
    }
}
