package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class TestDTO extends AbstractAuditingDTO {

    private Long id;

    private String description;

    @NotNull
    private String syllabus;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate postedDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate testDate;

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

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
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
        if (!(o instanceof TestDTO)) return false;
        TestDTO testDTO = (TestDTO) o;
        return Objects.equals(getId(), testDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "TestDTO{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", syllabus='" + syllabus + '\'' +
            ", postedDate=" + postedDate +
            ", testDate=" + testDate +
            ", timeTableUnitId=" + timeTableUnitId +
            '}';
    }
}

