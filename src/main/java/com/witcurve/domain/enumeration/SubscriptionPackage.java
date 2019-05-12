package com.witcurve.domain.enumeration;

public enum SubscriptionPackage {
    QUARTERLY("Quarterly", 3, 1),
    SEMI_ANNUALLY("Semi Annually", 6, 1),
    ANNUALLY("Annually", 12,  1);

    private final String name;
    private final int months;
    private final int cost;

    SubscriptionPackage(String full, int months, int cost) {
        this.name = full;
        this.months = months;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getMonths() {
        return months;
    }

    public int getCost() {
        return cost;
    }
}
