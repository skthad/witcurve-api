package com.witcurve.domain.enumeration;

public enum ConfigType {

    GRADING_DEFINITION("Grading definition"),
    REPEAT_ATTENDANCE("Repeat attendance");

    private final String name;

    ConfigType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
