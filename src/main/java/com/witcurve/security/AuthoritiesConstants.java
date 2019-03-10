package com.witcurve.security;

/**
 * Constants for WitCurve authorities used in Spring Security.
 */
public final class AuthoritiesConstants {

    // never visible in UI. One user across the whole database will have this role.
    public static final String SUPER_ADMIN = "SuperAdmin";

    // never visible in UI. One InstituteAdmin per institute.
    public static final String INSTITUTE_ADMIN = "InstituteAdmin";

    // never visible in UI. Automatically for students, this role will be assigned.
    public static final String PARENT = "Parent";

    // never visible on UI. Automatically for Staff type 'TEACHING', this role should be assigned
    public static final String FACULTY = "Faculty";

    // no permission can be assigned to user with this authority. Such users are not supposed to access the application anyways
    public static final String GROUND_STAFF = "GroundStaff";

    // non-editable list of authorities visible in all institutes
    public static final String HR_MANAGER = "HRManager";
    public static final String STUDENT_MANAGER = "StudentManager";
    public static final String ACADEMICS_MANAGER = "AcademicsManager";
    public static final String CURRICULUM_MANAGER = "CurriculumManager";

    private AuthoritiesConstants() {
    }
}
