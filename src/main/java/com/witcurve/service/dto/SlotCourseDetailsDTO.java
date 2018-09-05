package com.witcurve.service.dto;

import com.witcurve.domain.GeneralSlotDetails;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Objects;

public class SlotCourseDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private GeneralSlotDetailsDTO gsd;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private CourseDTO course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeneralSlotDetailsDTO getGsd() {
        return gsd;
    }

    public void setGsd(GeneralSlotDetailsDTO gsd) {
        this.gsd = gsd;
    }
    @NotNull
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(@NotNull DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @NotNull

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotCourseDetailsDTO that = (SlotCourseDetailsDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SlotCourseDetailsDTO{" +
            "id=" + id +
            ", gsd=" + gsd +
            ", dayOfWeek=" + dayOfWeek +
            ", course=" + course +
            '}';
    }
}
