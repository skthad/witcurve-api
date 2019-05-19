package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.witcurve.domain.enumeration.ModeOfPayment;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import static com.witcurve.service.util.WitCurveConstants.DEFAULT_DATE_FORMAT;

public class PayrollDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private PayrollCycleDTO payrollCycle;

    @NotNull
    private PayrollDetailsDTO payrollDetails;

    private Integer onTimeDays = 0;

    private Integer graceDays = 0;

    private Integer lateDays = 0;

    private Double leavesWithoutPay = 0d;

    @NotNull
    private Double paidDays = 0d;

    @NotNull
    private Double payableDays = 0d;

    @NotNull
    private Double basicSalary = 0d;

    private Double houseRentAllowance = 0d;

    private Double conveyanceAllowance = 0d;

    private Double medicalAllowance = 0d;

    private Double managerialAllowance = 0d;

    private Double leaveTravelAllowance = 0d;

    private Double providentFund = 0d;

    private Double professionalTax = 0d;

    private Double incomeTax = 0d;

    private Double bonus = 0d;

    private Double miscEarnings = 0d;

    private Double miscDeductions = 0d;

    @NotNull
    private ModeOfPayment modeOfPayment;

    private Integer checkNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate initiatedOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDate closedOn;


    public PayrollDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayrollCycleDTO getPayrollCycle() {
        return payrollCycle;
    }

    public void setPayrollCycle(PayrollCycleDTO payrollCycle) {
        this.payrollCycle = payrollCycle;
    }

    public PayrollDetailsDTO getPayrollDetails() {
        return payrollDetails;
    }

    public void setPayrollDetails(PayrollDetailsDTO payrollDetails) {
        this.payrollDetails = payrollDetails;
    }

    public Integer getOnTimeDays() {
        return onTimeDays;
    }

    public void setOnTimeDays(Integer onTimeDays) {
        this.onTimeDays = onTimeDays;
    }

    public Integer getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(Integer graceDays) {
        this.graceDays = graceDays;
    }

    public Integer getLateDays() {
        return lateDays;
    }

    public void setLateDays(Integer lateDays) {
        this.lateDays = lateDays;
    }

    public Double getLeavesWithoutPay() {
        return leavesWithoutPay;
    }

    public void setLeavesWithoutPay(Double leavesWithoutPay) {
        this.leavesWithoutPay = leavesWithoutPay;
    }

    public Double getPaidDays() {
        return paidDays;
    }

    public void setPaidDays(Double paidDays) {
        this.paidDays = paidDays;
    }

    public Double getPayableDays() {
        return payableDays;
    }

    public void setPayableDays(Double payableDays) {
        this.payableDays = payableDays;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Double getHouseRentAllowance() {
        return houseRentAllowance;
    }

    public void setHouseRentAllowance(Double houseRentAllowance) {
        this.houseRentAllowance = houseRentAllowance;
    }

    public Double getConveyanceAllowance() {
        return conveyanceAllowance;
    }

    public void setConveyanceAllowance(Double conveyanceAllowance) {
        this.conveyanceAllowance = conveyanceAllowance;
    }

    public Double getMedicalAllowance() {
        return medicalAllowance;
    }

    public void setMedicalAllowance(Double medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public Double getManagerialAllowance() {
        return managerialAllowance;
    }

    public void setManagerialAllowance(Double managerialAllowance) {
        this.managerialAllowance = managerialAllowance;
    }

    public Double getLeaveTravelAllowance() {
        return leaveTravelAllowance;
    }

    public void setLeaveTravelAllowance(Double leaveTravelAllowance) {
        this.leaveTravelAllowance = leaveTravelAllowance;
    }

    public Double getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(Double providentFund) {
        this.providentFund = providentFund;
    }

    public Double getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(Double professionalTax) {
        this.professionalTax = professionalTax;
    }

    public Double getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(Double incomeTax) {
        this.incomeTax = incomeTax;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getMiscEarnings() {
        return miscEarnings;
    }

    public void setMiscEarnings(Double miscEarnings) {
        this.miscEarnings = miscEarnings;
    }

    public Double getMiscDeductions() {
        return miscDeductions;
    }

    public void setMiscDeductions(Double miscDeductions) {
        this.miscDeductions = miscDeductions;
    }

    public ModeOfPayment getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(ModeOfPayment modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public Integer getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(Integer checkNumber) {
        this.checkNumber = checkNumber;
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
        return "PayrollDTO{" +
            "id=" + id +
            '}';
    }
}
