package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.domain.enumeration.ReportModelType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class ReportCardDesignDTO  extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String shortForm;

    @NotNull
    private ReportModelType modelType;

    @NotNull
    private ReportFieldType fieldType;

    private Integer order;

    private Integer marks;

    private Boolean showGradesOnly;

    private Boolean selected;

    @NotNull
    private Long schoolInfoId;

    @NotNull
    private Boolean activated = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortForm() {
        return shortForm;
    }

    public void setShortForm(String shortForm) {
        this.shortForm = shortForm;
    }

    public ReportModelType getModelType() {
        return modelType;
    }

    public void setModelType(ReportModelType modelType) {
        this.modelType = modelType;
    }

    public ReportFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(ReportFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public Boolean getShowGradesOnly() {
        return showGradesOnly;
    }

    public void setShowGradesOnly(Boolean showGradesOnly) {
        this.showGradesOnly = showGradesOnly;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportCardDesignDTO that = (ReportCardDesignDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReportCardDesignDTO{" +
            "id=" + id +
            '}';
    }
}
