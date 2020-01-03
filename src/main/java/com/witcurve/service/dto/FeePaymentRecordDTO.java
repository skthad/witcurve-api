package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.enumeration.FeePaymentType;
import com.witcurve.domain.enumeration.PaymentRecordType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class FeePaymentRecordDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String orderId;

    private String transactionId;

    @NotNull
    private PaymentRecordType type;

    @NotNull
    @Min(value = 0L)
    private Double totalAmount;

    @NotNull
    @Min(value = 0L)
    private Double penaltyAmount = 0.0;

    @NotNull
    private Long studentFeeStructureId;

    @NotNull
    private List<FeePaymentDetailDTO> feePaymentDetails;

   // @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate penaltyTransactionDate;

    @NotNull
    private FeePaymentType feePaymentType;

    private TransactionRecordDTO transactionRecordDTO;

    private LocalDate transactionDate;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getTransactionId() { return transactionId; }

    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public PaymentRecordType getType() { return type; }

    public void setType(PaymentRecordType type) { this.type = type; }

    public Double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getPenaltyAmount() { return penaltyAmount; }

    public void setPenaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    public Long getStudentFeeStructureId() { return studentFeeStructureId; }

    public void setStudentFeeStructureId(Long studentFeeStructureId) { this.studentFeeStructureId = studentFeeStructureId; }

    public List<FeePaymentDetailDTO> getFeePaymentDetails() { return feePaymentDetails; }

    public void setFeePaymentDetails(List<FeePaymentDetailDTO> feePaymentDetails) { this.feePaymentDetails = feePaymentDetails; }

    public LocalDate getPenaltyTransactionDate() { return penaltyTransactionDate; }

    public void setPenaltyTransactionDate(LocalDate penaltyTransactionDate) { this.penaltyTransactionDate = penaltyTransactionDate; }

    public TransactionRecordDTO getTransactionRecordDTO() { return transactionRecordDTO; }

    public void setTransactionRecordDTO(TransactionRecordDTO transactionRecordDTO) { this.transactionRecordDTO = transactionRecordDTO; }

    public FeePaymentType getFeePaymentType() { return feePaymentType; }

    public void setFeePaymentType(FeePaymentType feePaymentType) { this.feePaymentType = feePaymentType; }

    public LocalDate getTransactionDate() { return transactionDate; }

    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeePaymentRecordDTO that = (FeePaymentRecordDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeePaymentRecordDTO{" +
            "id=" + id +
            '}';
    }
}
