package com.witcurve.service.impl;

import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.ReportStatus;
import com.witcurve.service.dto.AbstractAuditingDTO;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private Attachment withHeader;

    @NotNull
    @OneToOne
    @JoinColumn(nullable = false)
    private Attachment withOutHeader;

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

    @Override
    public String toString() {
        return "StandardReportDTO{" +
            "id=" + id +
            '}';
    }
}
