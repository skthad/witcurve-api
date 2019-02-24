package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CourseDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String courseCode;

    private String description;

    private Boolean eligibleForSubstitute;

    @NotNull
    private Grade grade;

    @NotNull
    private String masterSubject;

    @NotNull
    private Long schoolInfoId;

    @NotNull
    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEligibleForSubstitute() {
        return eligibleForSubstitute;
    }

    public void setEligibleForSubstitute(Boolean eligibleForSubstitute) {
        this.eligibleForSubstitute = eligibleForSubstitute;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getMasterSubject() {
        return masterSubject;
    }

    public void setMasterSubject(String name) {
        this.masterSubject = name;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseDTO)) return false;
        CourseDTO courseDTO = (CourseDTO) o;
        return Objects.equals(getId(), courseDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + id +
            ", courseCode='" + courseCode + '\'' +
            ", description='" + description + '\'' +
            ", eligibleForSubstitute=" + eligibleForSubstitute +
            ", grade=" + grade +
            ", masterSubject='" + masterSubject + '\'' +
            ", schoolInfoId=" + schoolInfoId +
            ", active=" + active +
            '}';
    }
}
