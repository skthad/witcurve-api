package com.witcurve.domain.enumeration;

public enum FeePaymentType {

    FULL_YEAR_PAYMENT("Full Year Payment"), OUTSTANDING_FEE("Outstanding Fee");
    private final String value;

    private FeePaymentType(String value) {
        this.value = value;
    }

    public boolean equalsName(String value) {
        return value.equals(value);
    }

    public String toString() {
        return this.value;
    }

    public static FeePaymentType getFeePaymentType(String value) {
        if (value != null) {
            for (FeePaymentType m : values()) {
                if (m.toString().equals(value)) {
                    return m;
                }
            }
            return null;
        } else {
            return null;
        }
    }
}

