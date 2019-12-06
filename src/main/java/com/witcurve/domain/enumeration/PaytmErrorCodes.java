package com.witcurve.domain.enumeration;

public enum PaytmErrorCodes {

    SUCCESS(100),
    NO_DUE(101),
    INVALID_INSTITUTE_NAME(102),
    INVALID_ENROLLMENT_NUMBERS(103),
    INVALID_TYPE(104),
    INVALID_IP_ADDRESS_FOR_COMMUNICATION(105),
    INTERNAL_SERVER_ERROR(106),
    All_FIELDS_ARE_NOT_PRESENT(107),
    RECORD_ALREADY_EXIST(108),
    GIVEN_FEETYPE_OR_FEEDESCRIPTION_NOT_PRESENT(109);
    private final int value;

    PaytmErrorCodes(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}

