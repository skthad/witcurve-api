package com.witcurve.domain.enumeration;

public enum View {

    DAIRY("Dairy"),
    DAILY("Daily"),
    WEEK("Week"),
    MONTH("Month");

    private final String name;

    View(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
