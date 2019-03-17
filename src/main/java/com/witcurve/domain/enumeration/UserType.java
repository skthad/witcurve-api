package com.witcurve.domain.enumeration;

public enum UserType {
    SUPER_USER, //Only one user per db who has ultimate powerrrrrr.
    INSTITUTE_USER, // to be assigned to only one user per institute: with authority InstituteAdmin
    SCHOOL_USER,
    SCHOOL_INFO_USER,
    TEACHING_STAFF,
    NON_TEACHING_STAFF,
    GROUND_STAFF,
    PARENT
}
