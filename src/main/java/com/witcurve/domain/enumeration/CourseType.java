package com.witcurve.domain.enumeration;

public enum CourseType {
    SCHOLASTIC("Scholastic"),
    NON_SCHOLASTIC("Non-Scholastic");

        private final String value;

    private CourseType(String value) { this.value = value; }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static CourseType getGrade(String value) {
        if(value != null) {
            for(CourseType subjectType : values()) {
                if(subjectType.toString().equals(value)) {
                    return subjectType;
                }
            }
            return null;
        } else {
            return null;
        }

    }
}
