package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SurveyFormDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Length(max = 50, message = "The field name must be less than 50 characters")
    private String name;

    @Length(max = 1000, message = "The field description must be less than 1000 characters")
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

    private List<Long> standardIds;

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

    public List<Long> getStandardIds() { return standardIds; }

    public void setStandardIds(List<Long> standardIds) { this.standardIds = standardIds; }

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
