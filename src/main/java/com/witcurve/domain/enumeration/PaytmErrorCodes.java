package com.witcurve.domain.enumeration;

public enum PaytmErrorCodes {

    SUCCESS(100),
    NO_DUE(101),
    INVALID_INSTITUTE_NAME(102),
    INVALID_ENROLLMENT_NUMBERS(103),
    INVALID_TYPE(104),
    INVALID_IP_ADDRESS_FOR_COMMUNICATION(105),
    INTERNAL_SERVER_ERROR(106),
    MISSING_FIELDS(107),
    INVALID_FEE_NAME(108),
    INVALID_AMOUNT(109),
    INVALID_TRANSACTION_DATE(110),
    RECORD_ALREADY_EXIST(111);

    private final int value;

    PaytmErrorCodes(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

