package com.witcurve.service.dto;

import com.witcurve.domain.StudentStandard;

import java.util.Objects;

public class StudentStandardDTO extends AbstractAuditingDTO {

    private Long id;

    private StudentDTO student;

    private Long standardId;

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

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
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
        if (!(o instanceof StudentStandard)) return false;
        StudentStandard that = (StudentStandard) o;
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
            ", standardId=" + standardId +
            ", rollNo=" + rollNo +
            ", active='" + active + '\'' +
            '}';
    }
}
