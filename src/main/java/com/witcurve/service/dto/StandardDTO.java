package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class StandardDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private Grade grade;

    private String section;

    @NotNull
    private Long classTeacherId;

    @NotNull
    private TermDTO term;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Long getClassTeacherId() {
        return classTeacherId;
    }

    public void setClassTeacherId(Long classTeacherId) {
        this.classTeacherId = classTeacherId;
    }

    public TermDTO getTerm() {
        return term;
    }

    public void setTerm(TermDTO term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandardDTO that = (StandardDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StandardDTO{" +
            "id=" + id +
            ", grade=" + grade +
            ", section='" + section + '\'' +
            ", classTeacherId=" + classTeacherId +
            ", term=" + term +
            '}';
    }
}
