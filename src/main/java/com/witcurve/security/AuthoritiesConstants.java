package com.witcurve.security;

/**
 * Constants for WitCurve authorities used in Spring Security.
 */
public final class AuthoritiesConstants {

    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String INSTITUTE_ADMIN = "INSTITUTE_ADMIN";
    public static final String PARENT = "PARENT";
    public static final String FACULTY = "FACULTY";
    public static final String NON_TEACHING = "NON_TEACHING";

    private AuthoritiesConstants() {
    }
}
