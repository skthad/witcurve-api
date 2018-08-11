package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class HolidayDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String name;

    private LocalDate fromDate;

    private LocalDate toDate;

    private LocalDate holidayDate;

    @NotNull
    private Long academicSessionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public Long getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(Long academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HolidayDTO)) return false;
        HolidayDTO that = (HolidayDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "HolidayDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", fromDate=" + fromDate +
            ", toDate=" + toDate +
            ", holidayDate=" + holidayDate +
            ", academicSessionId=" + academicSessionId +
            '}';
    }
}
