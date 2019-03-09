package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class SiblingInfoDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private StudentDTO student;

    @NotNull
    private String name;

    @NotNull
    private String relationship;

    private String standardDuringAdmission;

    private LocalDate studyingSince;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getStandardDuringAdmission() {
        return standardDuringAdmission;
    }

    public void setStandardDuringAdmission(String standardDuringAdmission) {
        this.standardDuringAdmission = standardDuringAdmission;
    }

    public LocalDate getStudyingSince() {
        return studyingSince;
    }

    public void setStudyingSince(LocalDate studyingSince) {
        this.studyingSince = studyingSince;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiblingInfoDTO)) return false;
        SiblingInfoDTO siblingInfoDTO = (SiblingInfoDTO) o;
        return Objects.equals(getId(), siblingInfoDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "SiblingInfoDTO{" +
            "id=" + id +
            '}';
    }
}
