package com.witcurve.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    //public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String RESET_URL = "?context=reset-password&token=";

    // Mail template param keys
    public static final String PARAM_BASE_URL = "baseUrl";
    public static final String PARAM_RESET_URL = "resetUrl";
    public static final String PARAM_FULL_NAME = "fullName";
    public static final String PARAM_OTP = "otp";
    public static final String PARAM_SUBSCRIPTION_END_DATE = "subscriptionEndDate";
    public static final String PARAM_INSTITUTE_NAME = "instituteName";
    public static final String PARAM_SMS_SIGNATURE = "smsSignature";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_EVENT_NAME = "eventName";
    public static final String PARAM_TINY_URL = "tinyUrl";
    public static final String PARAM_MAIL_SUBJECT = "mailSubject";
    public static final String PARAM_TITLE_PLACEHOLDER = "titlePlaceholder";
    public static final String PARAM_GREETING_PLACEHOLDER = "greetingPlaceholder";




    private Constants() {
    }
}
