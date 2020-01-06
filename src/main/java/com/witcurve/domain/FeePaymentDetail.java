package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "fee_payment_detail")
public class FeePaymentDetail extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   // @NotNull
    @ManyToOne
    private FeeDetails feeType;

  //  @NotNull
    @ManyToOne
    private FeeDetails feeDescription;

    @NotNull
    @Column(nullable = false)
    private Double amount;

    @Column
    private String itemId;

    @Column(name = "transaction_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate transactionDate;

    @NotNull
    private Boolean isPenalty;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public FeeDetails getFeeType() { return feeType; }

    public void setFeeType(FeeDetails feeType) { this.feeType = feeType; }

    public FeeDetails getFeeDescription() { return feeDescription; }

    public void setFeeDescription(FeeDetails feeDescription) { this.feeDescription = feeDescription; }

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
        FeePaymentDetail that = (FeePaymentDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeePaymentDetail{" +
            "id=" + id +
            '}';
    }
}
