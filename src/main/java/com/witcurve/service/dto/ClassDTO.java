package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ClassDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private Grade grade;

    private String section;

    @NotNull
    private StaffDTO classTeacher;

    @NotNull
    private Long termId;

    @NotNull
    private Long schoolId;

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

    public StaffDTO getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(StaffDTO classTeacher) {
        this.classTeacher = classTeacher;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassDTO)) return false;
        ClassDTO classDTO = (ClassDTO) o;
        return Objects.equals(getId(), classDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ClassDTO{" +
            "id=" + id +
            ", grade='" + grade + '\'' +
            ", section='" + section + '\'' +
            ", classTeacher=" + classTeacher +
            ", termId=" + termId +
            ", schoolId=" + schoolId +
            '}';
    }
}
