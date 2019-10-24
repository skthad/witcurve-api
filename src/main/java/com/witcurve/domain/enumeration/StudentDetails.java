package com.witcurve.domain.enumeration;

public enum StudentDetails {

    STUDENT_NAME("studentName"),
    STANDARD("standard"),
    ADMISSION_ID("admissionId"),
    ATTENDANCE("attendance"),
    DATE_OF_BIRTH("dateOfBirth"),
    ROLL_NO("rollNo"),
    MOTHER_NAME("motherName"),
    FATHER_NAME("fatherName"),
    PLACE("place"),
    DATE("date");

    private final String name;

    StudentDetails(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
