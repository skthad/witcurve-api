package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.ReportFieldType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class ReportCardDesignDTO  extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String name;

    private String shortForm;

    @NotNull
    private ReportFieldType fieldType;

    private Integer order;

    private Double marks;

    private Boolean showGradesOnly;

    private Boolean showMarksOnly;

    private Boolean selected;

    private Long examId;

    private String bindingId;

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

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
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

    public Boolean getShowMarksOnly() {
        return showMarksOnly;
    }

    public void setShowMarksOnly(Boolean showMarksOnly) {
        this.showMarksOnly = showMarksOnly;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
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
