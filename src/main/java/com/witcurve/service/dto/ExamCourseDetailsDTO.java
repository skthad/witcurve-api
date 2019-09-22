package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class ExamCourseDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate date;

    private String description;

    @NotNull
    private GeneralSlotDetailsDTO gsd;

    @NotNull
    private CourseDTO course;

    private Boolean courseContentAttached = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public GeneralSlotDetailsDTO getGsd() {
        return gsd;
    }

    public void setGsd(GeneralSlotDetailsDTO gsd) {
        this.gsd = gsd;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public Boolean getCourseContentAttached() {
        return courseContentAttached;
    }

    public void setCourseContentAttached(Boolean courseContentAttached) {
        this.courseContentAttached = courseContentAttached;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamCourseDetailsDTO that = (ExamCourseDetailsDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ExamCourseDetailsDTO{" +
            '}';
    }
}
