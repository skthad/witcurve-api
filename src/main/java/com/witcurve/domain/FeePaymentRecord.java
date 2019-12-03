package com.witcurve.domain;

import com.witcurve.domain.enumeration.PaymentRecordType;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "fee_payment_record", uniqueConstraints = {
    @UniqueConstraint(name = "orderId_UK",
        columnNames = {"orderId"})
})
public class FeePaymentRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String orderId;

    @Column
    private String paytmId;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentRecordType type;

    @NotNull
    @Column(name = "total_amount")
    @Min(value = 0L, message = "total amount must be positive")
    private Double totalAmount;

    @NotNull
    @Column(name = "penalty_amount")
    @Min(value = 0L, message = "penalty amount must be positive")
    private Double penaltyAmount = 0.0;

    @NotNull
    @ManyToOne
    private StudentFeeStructure studentFeeStructure;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fee_payment_record_id")
    private List<FeePaymentDetail> feePaymentDetails;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate transactionDate;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public PaymentRecordType getType() { return type; }

    public void setType(PaymentRecordType type) { this.type = type; }

    public Double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getPenaltyAmount() { return penaltyAmount; }

    public void setPenaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public List<FeePaymentDetail> getFeePaymentDetails() { return feePaymentDetails; }

    public void setFeePaymentDetails(List<FeePaymentDetail> feePaymentDetails) { this.feePaymentDetails = feePaymentDetails; }

    public StudentFeeStructure getStudentFeeStructure() { return studentFeeStructure; }

    public void setStudentFeeStructure(StudentFeeStructure studentFeeStructure) { this.studentFeeStructure = studentFeeStructure; }

    public String getPaytmId() { return paytmId; }

    public void setPaytmId(String paytmId) { this.paytmId = paytmId; }

    public LocalDate getTransactionDate() { return transactionDate; }

    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeePaymentRecord that = (FeePaymentRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeePaymentRecord{" +
            "id=" + id +
            '}';
    }
}
