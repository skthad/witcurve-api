package com.witcurve.domain.enumeration;

public enum ModeOfPayment {

    CHEQUE("Cheque"),
    E_BANKING("E-Banking"),
    M_BANKING("M-Banking");

    private final String name;

    private ModeOfPayment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
