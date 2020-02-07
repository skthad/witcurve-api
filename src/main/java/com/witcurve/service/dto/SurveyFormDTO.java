package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class SurveyFormDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private SurveyFormCreator creator;

    @NotNull
    private SurveyUserType type;

    @NotNull
    private SurveyFormStatus status;

    @NotNull
    private Long schoolInfoId;

    private Boolean userSubmitted;

    private Set<SurveySectionDTO> sections;

    private Long standardId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SurveyFormCreator getCreator() {
        return creator;
    }

    public void setCreator(SurveyFormCreator creator) {
        this.creator = creator;
    }

    public SurveyUserType getType() {
        return type;
    }

    public void setType(SurveyUserType type) {
        this.type = type;
    }

    public SurveyFormStatus getStatus() {
        return status;
    }

    public void setStatus(SurveyFormStatus status) {
        this.status = status;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Boolean getUserSubmitted() {
        return userSubmitted;
    }

    public void setUserSubmitted(Boolean userSubmitted) {
        this.userSubmitted = userSubmitted;
    }

    public Set<SurveySectionDTO> getSections() {
        return sections;
    }

    public void setSections(Set<SurveySectionDTO> sections) {
        this.sections = sections;
    }

    public Long getStandardId() { return standardId; }

    public void setStandardId(Long standardId) { this.standardId = standardId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyFormDTO that = (SurveyFormDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SurveyFormDTO{" +
            "id=" + id +
            '}';
    }
}
