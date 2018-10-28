package com.witcurve.service.dto;

public class UserContextDTO {

    private UserDTO currentUser;

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }
}
