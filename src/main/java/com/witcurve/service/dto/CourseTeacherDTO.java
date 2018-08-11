package com.witcurve.service.dto;

import java.util.Objects;

public class CourseTeacherDTO {

    private Long id;

    private CourseDTO courseDTO;

    private StaffDTO teacherDTO;

    private Long classId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseDTO getCourseDTO() {
        return courseDTO;
    }

    public void setCourseDTO(CourseDTO courseDTO) {
        this.courseDTO = courseDTO;
    }

    public StaffDTO getTeacherDTO() {
        return teacherDTO;
    }

    public void setTeacherDTO(StaffDTO teacherDTO) {
        this.teacherDTO = teacherDTO;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
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
            ", courseDTO=" + courseDTO +
            ", teacherDTO=" + teacherDTO +
            ", classId=" + classId +
            '}';
    }
}
