package com.witcurve.service.dto;

import java.util.Objects;

public class CourseTeacherDTO {

    private Long id;

    private CourseDTO course;

    private StaffDTO teacher;

    private Long standardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public StaffDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(StaffDTO teacher) {
        this.teacher = teacher;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseTeacherDTO)) return false;
        CourseTeacherDTO that = (CourseTeacherDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "CourseTeacherDTO{" +
            "id=" + id +
            ", course=" + course +
            ", teacher=" + teacher +
            ", standardId=" + standardId +
            '}';
    }
}
