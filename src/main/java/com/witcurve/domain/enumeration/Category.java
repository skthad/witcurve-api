package com.witcurve.domain.enumeration;

public enum Category {

    GENERAL("General"),
    SC("SC"),
    ST("ST"),
    OBC("OBC"),
    NA("N/A"),
    OTHER("Other");

    private final String name;

    private Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
