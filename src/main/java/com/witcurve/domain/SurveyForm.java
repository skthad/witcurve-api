package com.witcurve.domain;

import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFromStatus;
import com.witcurve.domain.enumeration.SurveyUserType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "survey_form")
public class SurveyForm extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SurveyFormCreator creator;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SurveyUserType type;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SurveyFromStatus status;

    @ManyToOne
    private SchoolInfo schoolInfo;

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

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyForm that = (SurveyForm) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SurveyForm{" +
            "id=" + id +
            '}';
    }
}
