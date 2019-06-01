package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.SlotCourseDetails;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class SubstitutionDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate date;

    @NotNull
    private StaffDTO teacher;

    @NotNull
    private SlotCourseDetails scd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public StaffDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(StaffDTO teacher) {
        this.teacher = teacher;
    }

    public SlotCourseDetails getScd() {
        return scd;
    }

    public void setScd(SlotCourseDetails scd) {
        this.scd = scd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubstitutionDTO)) return false;
        SubstitutionDTO that = (SubstitutionDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "SubstitutionDTO{" +
            "id=" + id +
            '}';
    }
}
