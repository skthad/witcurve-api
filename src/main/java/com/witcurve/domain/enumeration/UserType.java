package com.witcurve.domain.enumeration;

public enum UserType {
    SUPER_USER, // Only one user per db who has ultimate powerrrrrr.
    INSTITUTE_MANAGER, // to be assigned to only one user per institute: with authority InstituteAdmin
    SCHOOL_MANAGER,
    SCHOOL_BOARD_MANAGER,
    TEACHING_STAFF,
    NON_TEACHING_STAFF,
    PARENT
}
