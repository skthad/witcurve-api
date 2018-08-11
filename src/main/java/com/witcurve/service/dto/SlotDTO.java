package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class SlotDTO extends AbstractAuditingDTO{
    private Long id;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private Long standardId;

    @NotNull
    private Integer position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SlotDTO)) return false;
        SlotDTO slotDTO = (SlotDTO) o;
        return Objects.equals(getId(), slotDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "SlotDTO{" +
            "id=" + id +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", standardId=" + standardId +
            ", position=" + position +
            '}';
    }
}
