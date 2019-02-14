package com.witcurve.domain.enumeration;

public enum ConfigFieldName {

    MARKS_PERCENTAGE_LTE("Percentage <="),
    ENABLE_DOUBLE_ATTENDANCE("Enable double attendance");

    private final String name;

    ConfigFieldName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
