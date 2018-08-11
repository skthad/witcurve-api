package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TimeTableUnitDTO extends AbstractAuditingDTO{

    private Long id;

    @NotNull
    private CourseTeacherDTO courseTeacherDTO;
    //courseDTO
    @NotNull
    private SlotDTO slotDTO;
    // need slotDTO

    @NotNull
    private Integer dayOfWeek;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseTeacherDTO getCourseTeacherDTO() {
        return courseTeacherDTO;
    }

    public void setCourseTeacherDTO(CourseTeacherDTO courseTeacherDTO) {
        this.courseTeacherDTO = courseTeacherDTO;
    }

    public SlotDTO getSlotDTO() {
        return slotDTO;
    }

    public void setSlotDTO(SlotDTO slotDTO) {
        this.slotDTO = slotDTO;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeTableUnitDTO)) return false;
        TimeTableUnitDTO that = (TimeTableUnitDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "TimeTableUnitDTO{" +
            "id=" + id +
            ", courseTeacherDTO=" + courseTeacherDTO +
            ", slotDTO=" + slotDTO +
            ", dayOfWeek=" + dayOfWeek +
            '}';
    }
}
