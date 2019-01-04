package com.witcurve.service.dto;

import java.util.Objects;

public class StudentStandardDTO extends AbstractAuditingDTO {

    private Long id;

    private StudentDTO student;

    private StandardDTO standard;

    private String rollNo;

    private Boolean active = true;

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

    public StandardDTO getStandard() {
        return standard;
    }

    public void setStandard(StandardDTO standardId) {
        this.standard = standard;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentStandardDTO)) return false;
        StudentStandardDTO that = (StudentStandardDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "StudentStandard{" +
            "id=" + id +
            ", studentId=" + student.getId() +
            (standard != null ? (", standardId=" + standard.getId()) : "") +
            ", rollNo=" + rollNo +
            ", active='" + active + '\'' +
            '}';
    }
}
