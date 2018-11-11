package com.witcurve.service.dto;

import java.util.Objects;

public class StudentMarksDTO extends AbstractAuditingDTO {

    private Long id;

    private Long studentId;

    private Long testId;

    private Integer marks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentMarksDTO)) return false;
        StudentMarksDTO that = (StudentMarksDTO) o;
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
            ", studentId=" + studentId +
            ", testId=" + testId +
            ", marks='" + marks+ '\'' +
            '}';
    }
}
