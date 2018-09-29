package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class ExamDetailsDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate examDate;

    @NotNull
    private Long courseId;

    @NotNull
    private Long standardId;

    @NotNull
    private Long examId;

    @NotNull
    private String syllabus;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExamDetailsDTO)) return false;
        ExamDetailsDTO that = (ExamDetailsDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ExamDetailsDTO{" +
            "id=" + id +
            ", examDate=" + examDate +
            ", courseId=" + courseId +
            ", standardId=" + standardId +
            ", examId=" + examId +
            ", syllabus='" + syllabus + '\'' +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
    }
}
