package com.witcurve.service.dto;

import com.witcurve.domain.Course;
import com.witcurve.domain.enumeration.Grade;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ReportCardDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Grade grade;

    @NotNull
    private Long examId;

    private Set<CourseDTO> scholasticCourses;

    private Set<CourseDTO> nonScholasticCourses;

    private Set<ReportCardDesignDTO> nonScholasticRcds;

    private List<ScholasticReportDetailsDTO> scholasticDetails;

    private Map<String, String> nonScholasticHeaderMap;

    @NotNull
    private Boolean showAttributes = false;

    @NotNull
    private Boolean showRemarks = false;

    @NotNull
    private Boolean showOverallMarks = false;

    @NotNull
    private Boolean showOverallGrade = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Set<CourseDTO> getScholasticCourses() {
        return scholasticCourses;
    }

    public void setScholasticCourses(Set<CourseDTO> scholasticCourses) {
        this.scholasticCourses = scholasticCourses;
    }

    public Set<CourseDTO> getNonScholasticCourses() {
        return nonScholasticCourses;
    }

    public void setNonScholasticCourses(Set<CourseDTO> nonScholasticCourses) {
        this.nonScholasticCourses = nonScholasticCourses;
    }

    public Set<ReportCardDesignDTO> getNonScholasticRcds() {
        return nonScholasticRcds;
    }

    public void setNonScholasticRcds(Set<ReportCardDesignDTO> nonScholasticRcds) {
        this.nonScholasticRcds = nonScholasticRcds;
    }

    public List<ScholasticReportDetailsDTO> getScholasticDetails() {
        return scholasticDetails;
    }

    public void setScholasticDetails(List<ScholasticReportDetailsDTO> scholasticDetails) {
        this.scholasticDetails = scholasticDetails;
    }

    public Map<String, String> getNonScholasticHeaderMap() {
        return nonScholasticHeaderMap;
    }

    public void setNonScholasticHeaderMap(Map<String, String> nonScholasticHeaderMap) {
        this.nonScholasticHeaderMap = nonScholasticHeaderMap;
    }

    public Boolean getShowAttributes() {
        return showAttributes;
    }

    public void setShowAttributes(Boolean showAttributes) {
        this.showAttributes = showAttributes;
    }

    public Boolean getShowRemarks() {
        return showRemarks;
    }

    public void setShowRemarks(Boolean showRemarks) {
        this.showRemarks = showRemarks;
    }

    public Boolean getShowOverallMarks() {
        return showOverallMarks;
    }

    public void setShowOverallMarks(Boolean showOverallMarks) {
        this.showOverallMarks = showOverallMarks;
    }

    public Boolean getShowOverallGrade() {
        return showOverallGrade;
    }

    public void setShowOverallGrade(Boolean showOverallGrade) {
        this.showOverallGrade = showOverallGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportCardDTO that = (ReportCardDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReportCardDTO{" +
            "id=" + id +
            '}';
    }
}
