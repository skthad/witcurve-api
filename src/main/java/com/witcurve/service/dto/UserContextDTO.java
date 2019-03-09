package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserContextDTO {

    private static final long serialVersionUID = 1L;

    private UserDTO currentUser;

    private TermDTO currentTerm;

    private AcademicSessionDTO currentAcademicSession;

    private List<EventDTO> holidayList;

    private StudentStandardDTO studentStandardDTO;

    private List<CourseTeacherDTO> studentCourses;

    private List<StandardDTO> staffStandards;

    private Map<Long, List<CourseDTO>> standardCourseMap;

    private LocalDate currentSessionStartDate;
    private LocalDate currentSessionEndDate;
    private LocalDate currentTermStartDate;
    private LocalDate currentTermEndDate;

    private long totalCalendarDaysInSession;
    private long totalWorkingDaysInSession;
    private long noOfCalendarDaysInSession;
    private long noOfWorkingDaysInSession;
    private long totalCalendarDaysInTerm;
    private long totalWorkingDaysInTerm;
    private long noOfCalendarDaysInTerm;
    private long noOfWorkingDaysInTerm;
    private long totalCalendarDaysInMonth;
    private long totalWorkingDaysInMonth;
    private long noOfCalendarDaysInMonth;
    private long noOfWorkingDaysInMonth;

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }

    public TermDTO getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(TermDTO currentTerm) {
        this.currentTerm = currentTerm;
    }

    public AcademicSessionDTO getCurrentAcademicSession() {
        return currentAcademicSession;
    }

    public void setCurrentAcademicSession(AcademicSessionDTO currentAcademicSession) {
        this.currentAcademicSession = currentAcademicSession;
    }

    public List<EventDTO> getHolidayList() {
        return holidayList;
    }

    public void setHolidayList(List<EventDTO> holidayList) {
        this.holidayList = holidayList;
    }

    public StudentStandardDTO getStudentStandardDTO() {
        return studentStandardDTO;
    }

    public void setStudentStandardDTO(StudentStandardDTO studentStandardDTO) {
        this.studentStandardDTO = studentStandardDTO;
    }

    public List<CourseTeacherDTO> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(List<CourseTeacherDTO> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public List<StandardDTO> getStaffStandards() {
        return staffStandards;
    }

    public void setStaffStandards(List<StandardDTO> staffStandards) {
        this.staffStandards = staffStandards;
    }

    public Map<Long, List<CourseDTO>> getStandardCourseMap() {
        return standardCourseMap;
    }

    public void setStandardCourseMap(Map<Long, List<CourseDTO>> standardCourseMap) {
        this.standardCourseMap = standardCourseMap;
    }

    public LocalDate getCurrentSessionStartDate() {
        return currentSessionStartDate;
    }

    public void setCurrentSessionStartDate(LocalDate currentSessionStartDate) {
        this.currentSessionStartDate = currentSessionStartDate;
    }

    public LocalDate getCurrentSessionEndDate() {
        return currentSessionEndDate;
    }

    public void setCurrentSessionEndDate(LocalDate currentSessionEndDate) {
        this.currentSessionEndDate = currentSessionEndDate;
    }

    public LocalDate getCurrentTermStartDate() {
        return currentTermStartDate;
    }

    public void setCurrentTermStartDate(LocalDate currentTermStartDate) {
        this.currentTermStartDate = currentTermStartDate;
    }

    public LocalDate getCurrentTermEndDate() {
        return currentTermEndDate;
    }

    public void setCurrentTermEndDate(LocalDate currentTermEndDate) {
        this.currentTermEndDate = currentTermEndDate;
    }

    public long getTotalCalendarDaysInSession() {
        return totalCalendarDaysInSession;
    }

    public void setTotalCalendarDaysInSession(long totalCalendarDaysInSession) {
        this.totalCalendarDaysInSession = totalCalendarDaysInSession;
    }

    public long getTotalWorkingDaysInSession() {
        return totalWorkingDaysInSession;
    }

    public void setTotalWorkingDaysInSession(long totalWorkingDaysInSession) {
        this.totalWorkingDaysInSession = totalWorkingDaysInSession;
    }

    public long getNoOfCalendarDaysInSession() {
        return noOfCalendarDaysInSession;
    }

    public void setNoOfCalendarDaysInSession(long noOfCalendarDaysInSession) {
        this.noOfCalendarDaysInSession = noOfCalendarDaysInSession;
    }

    public long getNoOfWorkingDaysInSession() {
        return noOfWorkingDaysInSession;
    }

    public void setNoOfWorkingDaysInSession(long noOfWorkingDaysInSession) {
        this.noOfWorkingDaysInSession = noOfWorkingDaysInSession;
    }

    public long getTotalCalendarDaysInTerm() {
        return totalCalendarDaysInTerm;
    }

    public void setTotalCalendarDaysInTerm(long totalCalendarDaysInTerm) {
        this.totalCalendarDaysInTerm = totalCalendarDaysInTerm;
    }

    public long getTotalWorkingDaysInTerm() {
        return totalWorkingDaysInTerm;
    }

    public void setTotalWorkingDaysInTerm(long totalWorkingDaysInTerm) {
        this.totalWorkingDaysInTerm = totalWorkingDaysInTerm;
    }

    public long getNoOfCalendarDaysInTerm() {
        return noOfCalendarDaysInTerm;
    }

    public void setNoOfCalendarDaysInTerm(long noOfCalendarDaysInTerm) {
        this.noOfCalendarDaysInTerm = noOfCalendarDaysInTerm;
    }

    public long getNoOfWorkingDaysInTerm() {
        return noOfWorkingDaysInTerm;
    }

    public void setNoOfWorkingDaysInTerm(long noOfWorkingDaysInTerm) {
        this.noOfWorkingDaysInTerm = noOfWorkingDaysInTerm;
    }

    public long getTotalCalendarDaysInMonth() {
        return totalCalendarDaysInMonth;
    }

    public void setTotalCalendarDaysInMonth(long totalCalendarDaysInMonth) {
        this.totalCalendarDaysInMonth = totalCalendarDaysInMonth;
    }

    public long getTotalWorkingDaysInMonth() {
        return totalWorkingDaysInMonth;
    }

    public void setTotalWorkingDaysInMonth(long totalWorkingDaysInMonth) {
        this.totalWorkingDaysInMonth = totalWorkingDaysInMonth;
    }

    public long getNoOfCalendarDaysInMonth() {
        return noOfCalendarDaysInMonth;
    }

    public void setNoOfCalendarDaysInMonth(long noOfCalendarDaysInMonth) {
        this.noOfCalendarDaysInMonth = noOfCalendarDaysInMonth;
    }

    public long getNoOfWorkingDaysInMonth() {
        return noOfWorkingDaysInMonth;
    }

    public void setNoOfWorkingDaysInMonth(long noOfWorkingDaysInMonth) {
        this.noOfWorkingDaysInMonth = noOfWorkingDaysInMonth;
    }
}
