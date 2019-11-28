package com.witcurve.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class SessionFeeDescriptionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long feeDescriptionId;

    @NotNull
    private Double amount;

    @NotNull
    private Boolean required = false;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getFeeDescriptionId() { return feeDescriptionId; }

    public void setFeeDescriptionId(Long feeDescriptionId) {
        this.feeDescriptionId = feeDescriptionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getRequired() { return required; }

    public void setRequired(Boolean required) { this.required = required; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionFeeDescriptionDTO that = (SessionFeeDescriptionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SessionFeeDescriptionDTO{" +
            "id=" + id +
            '}';
    }
}
