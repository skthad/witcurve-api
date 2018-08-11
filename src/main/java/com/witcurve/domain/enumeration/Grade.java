package com.witcurve.domain.enumeration;

public enum Grade {
    NURSERY("Nursery"),
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


    private final String name;

    private Grade(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
