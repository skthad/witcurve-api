package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.GSDStatus;
import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

public class GeneralSlotDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^([01]\\d|2[0-3])([0-5]\\d)$")
    private String start;

    @NotNull
    private Integer duration;

    @NotNull
    private Boolean recess;

    private StandardDTO standard;

    private Grade grade;

    @NotNull
    private GSDStatus status;

    private String bindingId;

    private Long examId;

    private String examName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
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

    public StandardDTO getStandard() {
        return standard;
    }

    public void setStandard(StandardDTO standard) {
        this.standard = standard;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public GSDStatus getStatus() {
        return status;
    }

    public void setStatus(GSDStatus status) {
        this.status = status;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
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
            (examId == null ? (", standard=" + standard) : "") +
            (examId != null ? (", grade=" + grade) : "") +
            (examId != null ? (", examId=" + examId) : "") +
            '}';
    }
}
