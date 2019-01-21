package com.witcurve.domain.enumeration;

public enum BloodGroup {

    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String name;

    private BloodGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
