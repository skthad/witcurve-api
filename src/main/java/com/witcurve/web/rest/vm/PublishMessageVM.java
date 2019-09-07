package com.witcurve.web.rest.vm;

import com.witcurve.domain.enumeration.TopicType;

import javax.validation.constraints.NotNull;

public class PublishMessageVM {

    @NotNull
    private String message;

    private String url;

    private String endPoint;

    private TopicType type;

    private Long schoolInfoId;

    private Long standardId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public TopicType getType() {
        return type;
    }

    public void setType(TopicType type) {
        this.type = type;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Long getStandardId() { return standardId; }

    public void setStandardId(Long standardId) { this.standardId = standardId; }

}
