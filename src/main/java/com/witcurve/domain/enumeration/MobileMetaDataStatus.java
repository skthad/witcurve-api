package com.witcurve.domain.enumeration;

public enum MobileMetaDataStatus {
    IOS("ios"),
    ANDROID("android");

    private final String value;

    private MobileMetaDataStatus(String value) {
        this.value = value;
    }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static MobileMetaDataStatus getMobileMetaDataStatus(String value) {
        if(value != null) {
            for(MobileMetaDataStatus m : values()) {
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
