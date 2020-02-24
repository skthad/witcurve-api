package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.witcurve.domain.enumeration.SurveyFormCreator;
import com.witcurve.domain.enumeration.SurveyFormStatus;
import com.witcurve.domain.enumeration.SurveyUserType;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "survey_form")
public class SurveyForm extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50, nullable = false)
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
    private SurveyFormStatus status;

    @NotNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private SchoolInfo schoolInfo;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="form_id", insertable = false, updatable = false)
    @OrderBy("section_order asc")
    private List<SurveySection> sections;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(
        name = "survey_form_standards",
        joinColumns = {@JoinColumn(name = "survey_form_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "standard_id", referencedColumnName = "id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OrderBy("grade asc,section asc")
    private List<Standard> standards;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="form_id", insertable = false, updatable = false)
    private List<SurveySubmission> surveySubmissions;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public List<SurveySection> getSections() {
        return sections;
    }

    public void setSections(List<SurveySection> sections) {
        this.sections = sections;
    }

    public List<Standard> getStandards() { return standards; }

    public void setStandards(List<Standard> standards) { this.standards = standards; }

    public List<SurveySubmission> getSurveySubmissions() { return surveySubmissions; }

    public void setSurveySubmissions(List<SurveySubmission> surveySubmissions) { this.surveySubmissions = surveySubmissions; }

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
