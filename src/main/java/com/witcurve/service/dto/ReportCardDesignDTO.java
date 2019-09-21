package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.CalculationType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ReportCardDesignDTO  extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String name;

    private String shortForm;

    @NotNull
    private ReportFieldType fieldType;

    private Integer order;

    private Double marks;

    private Boolean selected;

    private Long examId;

    private List<String> selectedPeriodicTests;

    private CalculationType calculationType;

    private Integer bestOfValue;

    private Grade grade;

    private Set<CourseDTO> courseDTOs;

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

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Set<CourseDTO> getCourseDTOs() {
        return courseDTOs;
    }

    public void setCourseDTOs(Set<CourseDTO> courseDTOs) {
        this.courseDTOs = courseDTOs;
    }

    public List<String> getSelectedPeriodicTests() {
        return selectedPeriodicTests;
    }

    public void setSelectedPeriodicTests(List<String> selectedPeriodicTests) {
        this.selectedPeriodicTests = selectedPeriodicTests;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

    public Integer getBestOfValue() {
        return bestOfValue;
    }

    public void setBestOfValue(Integer bestOfValue) {
        this.bestOfValue = bestOfValue;
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
