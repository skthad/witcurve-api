package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SessionFeeStructureDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long sessionId;

    @NotNull
    private Long feeTypeId;

    @NotNull
    private Grade grade;

    @NotNull
    private List<SessionFeeDescriptionDTO> sessionFeeDescriptions;

    private Double amount;

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

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public List<SessionFeeDescriptionDTO> getSessionFeeDescriptions() { return sessionFeeDescriptions; }

    public void setSessionFeeDescriptions(List<SessionFeeDescriptionDTO> sessionFeeDescriptions) { this.sessionFeeDescriptions = sessionFeeDescriptions; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

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
