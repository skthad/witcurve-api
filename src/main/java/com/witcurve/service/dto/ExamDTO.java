package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class ExamDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String name;


    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate endDate;

    @NotNull
    private String examType;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
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
        if (!(o instanceof ExamDTO)) return false;
        ExamDTO examDTO = (ExamDTO) o;
        return Objects.equals(getId(), examDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ExamDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", examType=" + examType +
            ", academicSessionId=" + academicSessionId +
            '}';
    }
}
