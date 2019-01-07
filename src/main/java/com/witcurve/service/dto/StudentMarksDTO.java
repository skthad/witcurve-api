package com.witcurve.service.dto;

import java.util.Objects;

public class StudentMarksDTO extends AbstractAuditingDTO {

    private Long id;

    private Long studentId;

    private Long eventId;

    private Integer marks;

    private Long examCourseDetailsId;

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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public Long getExamCourseDetailsId() {
        return examCourseDetailsId;
    }

    public void setExamCourseDetailsId(Long examCourseDetailsId) {
        this.examCourseDetailsId = examCourseDetailsId;
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
        return "StudentMarksDTO{" +
            "id=" + id +
            ", studentId=" + studentId +
            ", eventId=" + eventId +
            ", marks=" + marks +
            ", examCourseDetailsId=" + examCourseDetailsId +
            '}';
    }
}
