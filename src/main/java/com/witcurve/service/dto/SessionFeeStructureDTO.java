package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import com.witcurve.domain.enumeration.Grade;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class SessionFeeStructureDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long sessionId;

    @NotNull
    private Long feeTypeId;

    @NotNull
    private Long feeDescriptionId;

    @NotNull
    private Grade grade;

    @NotNull
    private Double amount;

    private LocalDate dueDate;

    private Double penalty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public Long getFeeDescriptionId() {
        return feeDescriptionId;
    }

    public void setFeeDescriptionId(Long feeDescriptionId) {
        this.feeDescriptionId = feeDescriptionId;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionFeeStructureDTO that = (SessionFeeStructureDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SessionFeeStructureDTO{" +
            "id=" + id +
            '}';
    }
}
