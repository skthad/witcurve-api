package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.ModeOfPayment;

import java.time.LocalDate;
import java.util.Objects;

public class PayrollDTO extends AbstractAuditingDTO {

    private Long id;

    private PayrollDetailsDTO payrollDetails;

    private Float bonus;

    private Float lossOfPay;

    private String reasonForLoss;

    private Integer checkNumber;

    private ModeOfPayment modeOfPayment;

    private LocalDate initiatedOn;

    private LocalDate closedOn;

    public PayrollDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayrollDetailsDTO getPayrollDetails() {
        return payrollDetails;
    }

    public void setPayrollDetails(PayrollDetailsDTO payrollDetails) {
        this.payrollDetails = payrollDetails;
    }

    public Float getBonus() {
        return bonus;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }

    public Float getLossOfPay() {
        return lossOfPay;
    }

    public void setLossOfPay(Float lossOfPay) {
        this.lossOfPay = lossOfPay;
    }

    public String getReasonForLoss() {
        return reasonForLoss;
    }

    public void setReasonForLoss(String reasonForLoss) {
        this.reasonForLoss = reasonForLoss;
    }

    public Integer getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(Integer checkNumber) {
        this.checkNumber = checkNumber;
    }

    public ModeOfPayment getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(ModeOfPayment modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public LocalDate getInitiatedOn() {
        return initiatedOn;
    }

    public void setInitiatedOn(LocalDate initiatedOn) {
        this.initiatedOn = initiatedOn;
    }

    public LocalDate getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(LocalDate closedOn) {
        this.closedOn = closedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayrollDTO)) return false;
        PayrollDTO payrollDTO = (PayrollDTO) o;
        return Objects.equals(getId(), payrollDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "PayrollDetailsDTO{" +
            "id=" + id +
            '}';
    }
}
