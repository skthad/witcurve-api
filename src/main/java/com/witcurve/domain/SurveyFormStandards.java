package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "survey_form_standards")
public class SurveyFormStandards implements Serializable {

    @Id
    @NotNull
    @Column(name = "survey_form_id", nullable = false)
    private Long surveyFormId;

    @Id
    @NotNull
    @Column(name = "standard_id", nullable = false)
    private Long standardId;

    public Long getSurveyFormId() {
        return surveyFormId;
    }

    public void setSurveyFormId(Long surveyFormId) {
        this.surveyFormId = surveyFormId;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyFormStandards that = (SurveyFormStandards) o;
        return Objects.equals(surveyFormId, that.surveyFormId) &&
            Objects.equals(standardId, that.standardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(surveyFormId, standardId);
    }

    @Override
    public String toString() {
        return "SurveyFormStandards{" +
            "surveyFormId=" + surveyFormId +
            ", standardId=" + standardId +
            '}';
    }
}
