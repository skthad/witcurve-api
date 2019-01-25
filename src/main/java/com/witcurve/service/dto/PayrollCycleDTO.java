package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class PayrollCycleDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private Long schoolInfoId;

    @NotNull
    private Month month;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2099)
    private Integer year;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate cycleStart;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate cycleEnd;

    public PayrollCycleDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getCycleStart() {
        return cycleStart;
    }

    public void setCycleStart(LocalDate cycleStart) {
        this.cycleStart = cycleStart;
    }

    public LocalDate getCycleEnd() {
        return cycleEnd;
    }

    public void setCycleEnd(LocalDate cycleEnd) {
        this.cycleEnd = cycleEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayrollCycleDTO)) return false;
        PayrollCycleDTO payrollDTO = (PayrollCycleDTO) o;
        return Objects.equals(getId(), payrollDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "PayrollCycleDTO{" +
            "id=" + id +
            ", schoolInfoId='" + schoolInfoId + '\'' +
            ", month='" + month + '\'' +
            ", year='" + year + '\'' +
            ", cycleStart='" + cycleStart + '\'' +
            ", cycleEnd='" + cycleEnd + '\'' +
            '}';
    }
}
