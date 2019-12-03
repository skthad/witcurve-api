package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class FeePaymentDetailDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private Long feeTypeId;

    @NotNull
    private Long feeDescriptionId;

    @NotNull
    private Double amount;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate transactionDate;


    public Long getFeeTypeId() { return feeTypeId; }

    public void setFeeTypeId(Long feeTypeId) { this.feeTypeId = feeTypeId; }

    public Long getFeeDescriptionId() { return feeDescriptionId; }

    public void setFeeDescriptionId(Long feeDescriptionId) { this.feeDescriptionId = feeDescriptionId; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getTransactionDate() { return transactionDate; }

    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

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
