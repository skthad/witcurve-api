package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFromStatus;
import com.witcurve.domain.enumeration.SurveyUserType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

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
    private SurveyFromStatus status;

    private Long schoolInfoId;

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

    public SurveyFromStatus getStatus() {
        return status;
    }

    public void setStatus(SurveyFromStatus status) {
        this.status = status;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

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
