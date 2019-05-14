package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class StudentMarksDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long studentId;

    private String studentName;

    private Long standardId;

    private Grade grade;

    private String section;

    private String rollNo;

    private EventDTO eventDTO;

    @NotNull
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
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

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
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
            '}';
    }
}
