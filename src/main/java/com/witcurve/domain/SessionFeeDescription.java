package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "session_fee_description",  uniqueConstraints = {
    @UniqueConstraint(name = "fee_description_id_session_fee_structure_id_UK",
        columnNames = {"fee_description_id","session_fee_structure_id"})
})
public class SessionFeeDescription  extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private FeeDetails feeDescription;

    @NotNull
    @Column(nullable = false)
    @Min(value = 0L, message = "amount must be positive")
    private Double amount;

    @NotNull
    @Column(nullable = false)
    private Boolean required = false;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public FeeDetails getFeeDescription() { return feeDescription; }

    public void setFeeDescription(FeeDetails feeDescription) { this.feeDescription = feeDescription; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

    public Boolean getRequired() { return required; }

    public void setRequired(Boolean required) { this.required = required; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionFeeDescription that = (SessionFeeDescription) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SessionFeeDescription{" +
            "id=" + id +
            '}';
    }
}
