package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;

public class SlotCourseDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long gsdId;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private Long courseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public Long getGsdId() {
        return gsdId;
    }

    public void setGsdId(@NotNull Long gsdId) {
        this.gsdId = gsdId;
    }

    @NotNull
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(@NotNull DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @NotNull
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(@NotNull Long courseId) {
        this.courseId = courseId;
    }


}
