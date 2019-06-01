package com.witcurve.domain.enumeration;

public enum Grade {
    NURSERY("NURSERY"),
    LKG("LKG"),
    UKG("UKG"),
    I("I"),
    II("II"),
    III("III"),
    IV("IV"),
    V("V"),
    VI("VI"),
    VII("VII"),
    VIII("VIII"),
    IX("IX"),
    X("X"),
    XI("XI"),
    XII("XII");


    private final String value;

    private Grade(String value) {
        this.value = value;
    }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static Grade getGrade(String value) {
        if(value != null) {
            for(Grade m : values()) {
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
