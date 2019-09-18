package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class StudentCourseDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long studentStandardId;

    @NotNull
    private Long courseId;

    @NotNull
    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentStandardId() {
        return studentStandardId;
    }

    public void setStudentStandardId(Long studentStandardId) {
        this.studentStandardId = studentStandardId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourseDTO that = (StudentCourseDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentCourseDTO{" +
            "id=" + id +
            ", studentStandardId=" + studentStandardId +
            ", courseId=" + courseId +
            ", active=" + active +
            '}';
    }
}
