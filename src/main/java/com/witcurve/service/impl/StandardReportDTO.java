package com.witcurve.service.impl;

import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.ReportStatus;
import com.witcurve.service.dto.AbstractAuditingDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class StandardReportDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long reportCardId;

    @NotNull
    private Long standardId;

    @NotNull
    private ReportStatus status;

    private Attachment withHeader;

    private Attachment withOutHeader;

    private String failureReason;

    private String standardName;

    private String examName;

    private Boolean isExists = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportCardId() {
        return reportCardId;
    }

    public void setReportCardId(Long reportCardId) {
        this.reportCardId = reportCardId;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Attachment getWithHeader() {
        return withHeader;
    }

    public void setWithHeader(Attachment withHeader) {
        this.withHeader = withHeader;
    }

    public Attachment getWithOutHeader() {
        return withOutHeader;
    }

    public void setWithOutHeader(Attachment withOutHeader) {
        this.withOutHeader = withOutHeader;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Boolean getExists() { return isExists; }

    public void setExists(Boolean exists) { isExists = exists; }

    @Override
    public String toString() {
        return "StandardReportDTO{" +
            "id=" + id +
            '}';
    }
}
