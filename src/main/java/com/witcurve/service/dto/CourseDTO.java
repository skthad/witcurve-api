package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CourseDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String courseName;

    private String description;

    private Boolean eligibleForSubstitute;

    @NotNull
    private String masterSubject;

    @NotNull
    private Long schoolInfoId;

    private String subject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
            ", courseName='" + courseName + '\'' +
            ", description='" + description + '\'' +
            ", eligibleForSubstitute=" + eligibleForSubstitute +
            ", masterSubject=" + masterSubject +
            ", schoolInfoId=" + schoolInfoId +
            ", subject=" + subject +
            '}';
    }
}
