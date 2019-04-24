package com.witcurve.domain.enumeration;

public enum Category {

    GENERAL("GENERAL"),
    SC("SC"),
    ST("ST"),
    OBC("OBC"),
    NA("N/A"),
    OTHER("OTHER");

    private final String value;

    private Category(String value) {
        this.value = value;
    }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static Category getCategory(String value) {
        if(value != null) {
            for(Category m : values()) {
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
