package com.witcurve.domain.enumeration;

public enum MobileOsType {
    IOS("ios"),
    ANDROID("android");

    private final String value;

    private MobileOsType(String value) {
        this.value = value;
    }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static MobileOsType getMobileMetaDataStatus(String value) {
        if(value != null) {
            for(MobileOsType m : values()) {
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
