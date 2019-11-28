package com.witcurve.service.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class FeePaymentDetailDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long feeTypeId;

    @NotNull
    private Long feeDescriptionId;

    @NotNull
    private Double amount;

    public Long getFeeTypeId() { return feeTypeId; }

    public void setFeeTypeId(Long feeTypeId) { this.feeTypeId = feeTypeId; }

    public Long getFeeDescriptionId() { return feeDescriptionId; }

    public void setFeeDescriptionId(Long feeDescriptionId) { this.feeDescriptionId = feeDescriptionId; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeePaymentDetailDTO that = (FeePaymentDetailDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeePaymentDetailDTO{" +
            "id=" + id +
            '}';
    }
}
