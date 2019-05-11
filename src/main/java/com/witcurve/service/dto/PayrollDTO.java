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

    @NotNull
    private Integer paidDays = 0;

    @NotNull
    private Integer totalDays = 0;

    @NotNull
    private Float basicSalary = 0f;

    private Float houseRentAllowance;

    private Float conveyanceAllowance;

    private Float medicalAllowance;

    private Float managerialAllowance;

    private Float leaveTravelAllowance;

    private Float providentFund;

    private Float professionalTax;

    private Float incomeTax;

    private Float lateEntryDeductions;

    private Float bonus;

    private Float miscEarnings;

    private Float miscDeductions;

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

    public Integer getPaidDays() {
        return paidDays;
    }

    public void setPaidDays(Integer paidDays) {
        this.paidDays = paidDays;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public Float getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Float getHouseRentAllowance() {
        return houseRentAllowance;
    }

    public void setHouseRentAllowance(Float houseRentAllowance) {
        this.houseRentAllowance = houseRentAllowance;
    }

    public Float getConveyanceAllowance() {
        return conveyanceAllowance;
    }

    public void setConveyanceAllowance(Float conveyanceAllowance) {
        this.conveyanceAllowance = conveyanceAllowance;
    }

    public Float getMedicalAllowance() {
        return medicalAllowance;
    }

    public void setMedicalAllowance(Float medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public Float getManagerialAllowance() {
        return managerialAllowance;
    }

    public void setManagerialAllowance(Float managerialAllowance) {
        this.managerialAllowance = managerialAllowance;
    }

    public Float getLeaveTravelAllowance() {
        return leaveTravelAllowance;
    }

    public void setLeaveTravelAllowance(Float leaveTravelAllowance) {
        this.leaveTravelAllowance = leaveTravelAllowance;
    }

    public Float getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(Float providentFund) {
        this.providentFund = providentFund;
    }

    public Float getProfessionalTax() {
        return professionalTax;
    }

    public void setProfessionalTax(Float professionalTax) {
        this.professionalTax = professionalTax;
    }

    public Float getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(Float incomeTax) {
        this.incomeTax = incomeTax;
    }

    public Float getLateEntryDeductions() {
        return lateEntryDeductions;
    }

    public void setLateEntryDeductions(Float lateEntryDeductions) {
        this.lateEntryDeductions = lateEntryDeductions;
    }

    public Float getBonus() {
        return bonus;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }

    public Float getMiscEarnings() {
        return miscEarnings;
    }

    public void setMiscEarnings(Float miscEarnings) {
        this.miscEarnings = miscEarnings;
    }

    public Float getMiscDeductions() {
        return miscDeductions;
    }

    public void setMiscDeductions(Float miscDeductions) {
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
