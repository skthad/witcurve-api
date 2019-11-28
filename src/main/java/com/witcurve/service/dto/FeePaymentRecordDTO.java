package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.PaymentRecordType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class FeePaymentRecordDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String orderId;

    private String paytmId;

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

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getOrderId() { return orderId; }

    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getPaytmId() { return paytmId; }

    public void setPaytmId(String paytmId) { this.paytmId = paytmId; }

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
