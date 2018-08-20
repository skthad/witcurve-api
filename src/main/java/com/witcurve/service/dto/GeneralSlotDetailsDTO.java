package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

public class GeneralSlotDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "([01]?[0-9]|2[0-3])[0-5][0-9]")
    private String start;

    @NotNull
    private Integer duration;

    @NotNull
    private Boolean recess;

    @NotNull
    private Long standardId;

    private Long examId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getStart() {
        return start;
    }

    public void setStart(@NotNull String start) {
        this.start = start;
    }

    @NotNull
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(@NotNull Integer duration) {
        this.duration = duration;
    }

    @NotNull
    public Boolean getRecess() {
        return recess;
    }

    public void setRecess(@NotNull Boolean recess) {
        this.recess = recess;
    }

    @NotNull
    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(@NotNull Long standardId) {
        this.standardId = standardId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneralSlotDetailsDTO that = (GeneralSlotDetailsDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "GeneralSlotDetailsDTO{" +
            "id=" + id +
            ", start='" + start + '\'' +
            ", duration=" + duration +
            ", recess=" + recess +
            ", standardId=" + standardId +
            ", examId=" + examId +
            '}';
    }
}
