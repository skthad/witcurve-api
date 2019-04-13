package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class EventContentDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long eventId;

    @NotNull
    private CourseContentDTO courseContent;

    @NotNull
    private Boolean forExam = Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public CourseContentDTO getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(CourseContentDTO courseContent) {
        this.courseContent = courseContent;
    }

    public Boolean getForExam() {
        return forExam;
    }

    public void setForExam(Boolean forExam) {
        this.forExam = forExam;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventContentDTO)) return false;
        EventContentDTO eventContentDTO = (EventContentDTO) o;
        return Objects.equals(getId(), eventContentDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "EventContentDTO{" +
            "id=" + id +
            '}';
    }
}
