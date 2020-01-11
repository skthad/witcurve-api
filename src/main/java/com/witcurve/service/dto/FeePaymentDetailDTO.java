package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
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

    private Long feeDescriptionId;

    @NotNull
    private Double amount;

    private String itemId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate transactionDate;

    @NotNull
    private Boolean isPenalty = false;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getFeeTypeId() { return feeTypeId; }

    public void setFeeTypeId(Long feeTypeId) { this.feeTypeId = feeTypeId; }

    public Long getFeeDescriptionId() { return feeDescriptionId; }

    public void setFeeDescriptionId(Long feeDescriptionId) { this.feeDescriptionId = feeDescriptionId; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

    public String getItemId() { return itemId; }

    public void setItemId(String itemId) { this.itemId = itemId; }

    public LocalDate getTransactionDate() { return transactionDate; }

    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public Boolean getPenalty() { return isPenalty; }

    public void setPenalty(Boolean penalty) { isPenalty = penalty; }

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
