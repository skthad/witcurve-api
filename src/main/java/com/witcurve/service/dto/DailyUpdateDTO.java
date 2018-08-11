package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class DailyUpdateDTO extends AbstractAuditingDTO{

    private Long id;

    @NotNull
    private String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate postedDate;

    @NotNull
    private Long timeTableUnitId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Long getTimeTableUnitId() {
        return timeTableUnitId;
    }

    public void setTimeTableUnitId(Long timeTableUnitId) {
        this.timeTableUnitId = timeTableUnitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyUpdateDTO)) return false;
        DailyUpdateDTO that = (DailyUpdateDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "DailyUpdateDTO{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", postedDate=" + postedDate +
            ", timeTableUnitId=" + timeTableUnitId +
            '}';
    }
}
