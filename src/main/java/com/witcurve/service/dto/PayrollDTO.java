package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.ModeOfPayment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

public class PayrollDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private PayrollDetailsDTO payrollDetails;

    private Float bonus = 0f;

    private Float lossOfPay = 0f;

    private String reasonForLoss;

    private Integer checkNumber;

    @NotNull
    private ModeOfPayment modeOfPayment;

    private LocalDate initiatedOn;

    private LocalDate closedOn;

    @NotNull
    private Month month;

    @NotNull
    @Pattern(regexp = "^[1-2]\\d{3}$")
    private Integer year;

    @NotNull
    private Long staffId;

    @NotNull
    private Long sessionId;

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

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
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
