package com.witcurve.domain.enumeration;

public enum StudentType {
    DAY_SCHOLAR("DAY SCHOLAR"),
    RESIDENTIAL("RESIDENTIAL"),
    SEMI_RESIDENTIAL("SEMI-RESIDENTIAL");

    private final String value;

    private StudentType(String value) {
        this.value = value;
    }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static StudentType getStudentType(String value) {
        if(value != null) {
            for(StudentType m : values()) {
                if(m.toString().equals(value)) {
                    return m;
                }
            }
            return null;
        } else {
            return null;
        }

    }


}
