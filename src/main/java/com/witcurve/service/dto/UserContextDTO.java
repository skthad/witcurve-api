package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserContextDTO {

    private UserDTO currentUser;

    private TermDTO currentTerm;

    private AcademicSessionDTO currentAcademicSession;

    private StudentStandardDTO studentStandardDTO;

    private List<CourseTeacherDTO> studentCourses;

    private List<StandardDTO> staffStandards;

    private Map<Long, List<CourseDTO>> standardCourseMap;

    private long numberOfCalendarDaysInSession;

    private long numberOfCalendarDaysInTerm;

    private long numberOfCalendarDaysInMonth;

    private long numberOfWorkingDaysInSession;

    private long numberOfWorkingDaysInTerm;

    private long numberOfWorkingDaysInMonth;

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

    public long getNumberOfCalendarDaysInSession() {
        return numberOfCalendarDaysInSession;
    }

    public void setNumberOfCalendarDaysInSession(long numberOfCalendarDaysInSession) {
        this.numberOfCalendarDaysInSession = numberOfCalendarDaysInSession;
    }

    public long getNumberOfCalendarDaysInTerm() {
        return numberOfCalendarDaysInTerm;
    }

    public void setNumberOfCalendarDaysInTerm(long numberOfCalendarDaysInTerm) {
        this.numberOfCalendarDaysInTerm = numberOfCalendarDaysInTerm;
    }

    public long getNumberOfCalendarDaysInMonth() {
        return numberOfCalendarDaysInMonth;
    }

    public void setNumberOfCalendarDaysInMonth(long numberOfCalendarDaysInMonth) {
        this.numberOfCalendarDaysInMonth = numberOfCalendarDaysInMonth;
    }

    public long getNumberOfWorkingDaysInSession() {
        return numberOfWorkingDaysInSession;
    }

    public void setNumberOfWorkingDaysInSession(long numberOfWorkingDaysInSession) {
        this.numberOfWorkingDaysInSession = numberOfWorkingDaysInSession;
    }

    public long getNumberOfWorkingDaysInTerm() {
        return numberOfWorkingDaysInTerm;
    }

    public void setNumberOfWorkingDaysInTerm(long numberOfWorkingDaysInTerm) {
        this.numberOfWorkingDaysInTerm = numberOfWorkingDaysInTerm;
    }

    public long getNumberOfWorkingDaysInMonth() {
        return numberOfWorkingDaysInMonth;
    }

    public void setNumberOfWorkingDaysInMonth(long numberOfWorkingDaysInMonth) {
        this.numberOfWorkingDaysInMonth = numberOfWorkingDaysInMonth;
    }
}
