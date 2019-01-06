package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserContextDTO {

    private UserDTO currentUser;

    private StudentStandardDTO studentStandardDTO;

    private List<CourseTeacherDTO> studentCourses;

    private List<StandardDTO> staffStandards;

    private Map<Long, List<CourseDTO>> standardCourseMap;

    private Integer numberOfWorkingDays;

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
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

    public Integer getNumberOfWorkingDays() {
        return numberOfWorkingDays;
    }

    public void setNumberOfWorkingDays(Integer numberOfWorkingDays) {
        this.numberOfWorkingDays = numberOfWorkingDays;
    }
}
