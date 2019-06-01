package com.witcurve.domain.enumeration;

public enum StaffType {
    TEACHING("TEACHING"),
    NON_TEACHING("NON TEACHING");

    private final String value;

    private StaffType(String value) {
        this.value = value;
    }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static StaffType getStaffType(String value) {
        if(value != null) {
            for(StaffType m : values()) {
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
