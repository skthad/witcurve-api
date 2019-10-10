package com.witcurve.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class ScholasticReportDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean showGrades = false;

    @NotNull
    private Boolean showMarks = false;

    @NotNull
    private Double marksNormalisation;

    @NotNull
    private Integer scholasticOrder;

    private String header;

    @NotNull
    private Long reportCardDesignId;

    private Long reportCardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getShowGrades() {
        return showGrades;
    }

    public void setShowGrades(Boolean showGrades) {
        this.showGrades = showGrades;
    }

    public Boolean getShowMarks() {
        return showMarks;
    }

    public void setShowMarks(Boolean showMarks) {
        this.showMarks = showMarks;
    }

    public Double getMarksNormalisation() {
        return marksNormalisation;
    }

    public void setMarksNormalisation(Double marksNormalisation) {
        this.marksNormalisation = marksNormalisation;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Long getReportCardDesignId() {
        return reportCardDesignId;
    }

    public void setReportCardDesignId(Long reportCardDesignId) {
        this.reportCardDesignId = reportCardDesignId;
    }

    public Long getReportCardId() {
        return reportCardId;
    }

    public void setReportCardId(Long reportCardId) {
        this.reportCardId = reportCardId;
    }

    public Integer getScholasticOrder() {
        return scholasticOrder;
    }

    public void setScholasticOrder(Integer scholasticOrder) {
        this.scholasticOrder = scholasticOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScholasticReportDetailsDTO that = (ScholasticReportDetailsDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ScholasticReportDetailsDTO{" +
            "id=" + id +
            '}';
    }
}
