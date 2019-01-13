package com.witcurve.service.dto;

import java.util.Objects;

public class StudentMarksDTO extends AbstractAuditingDTO {

    private Long id;

    private Long studentId;

    private EventDTO eventDTO;

    private Integer marks;

    private ExamCourseDetailsDTO examCourseDetailsDTO;

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

    public EventDTO getEventDTO() {
        return eventDTO;
    }

    public void setEventDTO(EventDTO eventDTO) {
        this.eventDTO = eventDTO;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public ExamCourseDetailsDTO getExamCourseDetailsDTO() {
        return examCourseDetailsDTO;
    }

    public void setExamCourseDetailsDTO(ExamCourseDetailsDTO examCourseDetailsDTO) {
        this.examCourseDetailsDTO = examCourseDetailsDTO;
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
            ", eventDTO=" + eventDTO +
            ", marks=" + marks +
            ", examCourseDetailsDTO=" + examCourseDetailsDTO +
            '}';
    }
}
