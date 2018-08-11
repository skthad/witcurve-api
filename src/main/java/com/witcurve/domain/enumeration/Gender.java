package com.witcurve.domain.enumeration;

public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private final String name;

    private Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
