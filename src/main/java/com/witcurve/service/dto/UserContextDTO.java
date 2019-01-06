package com.witcurve.service.dto;

import java.util.List;

public class UserContextDTO {

    private UserDTO currentUser;

    private StudentStandardDTO studentStandardDTO;

    private List<StandardDTO> staffStandardDTOs;

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

    public List<StandardDTO> getStaffStandardDTOs() {
        return staffStandardDTOs;
    }

    public void setStaffStandardDTOs(List<StandardDTO> staffStandardDTOs) {
        this.staffStandardDTOs = staffStandardDTOs;
    }

    public Integer getNumberOfWorkingDays() {
        return numberOfWorkingDays;
    }

    public void setNumberOfWorkingDays(Integer numberOfWorkingDays) {
        this.numberOfWorkingDays = numberOfWorkingDays;
    }
}
