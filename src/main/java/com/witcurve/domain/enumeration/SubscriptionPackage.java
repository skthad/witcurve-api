package com.witcurve.domain.enumeration;

public enum SubscriptionPackage {
    QUARTERLY("Quarterly", 3),
    SEMI_ANNUALLY("Semi Annually", 6),
    ANNUALLY("Annually", 12);

    private final String name;
    private final int months;

    SubscriptionPackage(String full, int months) {
        this.name = full;
        this.months = months;
    }

    public String getName() {
        return name;
    }

    public int getMonths() {
        return months;
    }
}
