package com.witcurve.domain.enumeration;

public enum GSDStatus {

    DRAFT("Draft"),
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String name;

    private GSDStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
