package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.ConfigFieldName;

public class NotificationInfo {

    private Boolean sendSMS = false;

    private Boolean sendEmail = false;

    private Boolean sendPush = false;

    private String instituteName = "";

    private Long schoolId;

    private String smsSignature = "";

    private ConfigFieldName fieldName;

    public Boolean getSendSMS() {
        return sendSMS;
    }

    public void setSendSMS(Boolean sendSMS) {
        this.sendSMS = sendSMS;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean getSendPush() {
        return sendPush;
    }

    public void setSendPush(Boolean sendPush) {
        this.sendPush = sendPush;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSmsSignature() {
        return smsSignature;
    }

    public void setSmsSignature(String smsSignature) {
        this.smsSignature = smsSignature;
    }

    public ConfigFieldName getFieldName() {
        return fieldName;
    }

    public void setFieldName(ConfigFieldName fieldName) {
        this.fieldName = fieldName;
    }
}
