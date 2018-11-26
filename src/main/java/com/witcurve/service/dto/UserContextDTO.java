package com.witcurve.service.dto;

import com.witcurve.domain.AcademicSession;

import java.util.List;

public class UserContextDTO {

    private UserDTO currentUser;

    private List<AcademicSession> currentSessions;

    private Integer numberOfWorkingDays;

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }

    public List<AcademicSession> getCurrentSessions() {
        return currentSessions;
    }

    public void setCurrentSessions(List<AcademicSession> currentSessions) {
        this.currentSessions = currentSessions;
    }

    public Integer getNumberOfWorkingDays() {
        return numberOfWorkingDays;
    }

    public void setNumberOfWorkingDays(Integer numberOfWorkingDays) {
        this.numberOfWorkingDays = numberOfWorkingDays;
    }
}
