package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "student_fee_description")
public class StudentFeeDescription extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private FeeDetails feeDescription;

    @NotNull
    private Double amount;

    @NotNull
    private Double adjustment;

    @NotNull
    private Double oneTimeDiscount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeeDetails getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(FeeDetails feeDescription) {
        this.feeDescription = feeDescription;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(Double adjustment) {
        this.adjustment = adjustment;
    }

    public Double getOneTimeDiscount() {
        return oneTimeDiscount;
    }

    public void setOneTimeDiscount(Double oneTimeDiscount) {
        this.oneTimeDiscount = oneTimeDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentFeeDescription that = (StudentFeeDescription) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentFeeDescription{" +
            "id=" + id +
            '}';
    }
}
