package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class NonScholasticReportDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer nonScholasticOrder;

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

    public Integer getNonScholasticOrder() {
        return nonScholasticOrder;
    }

    public void setNonScholasticOrder(Integer nonScholasticOrder) {
        this.nonScholasticOrder = nonScholasticOrder;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonScholasticReportDetailsDTO that = (NonScholasticReportDetailsDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "NonScholasticReportDetailsDTO{" +
            "id=" + id +
            '}';
    }
}
