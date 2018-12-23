package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Objects;

public class PayrollDetailsDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private StaffDTO staff;

    private Instant deactivationDate;

    private Float basicSalary = 0f;

    private Float houseRentAllowance = 0f;

    private Float conveyanceAllowance = 0f;

    private Float medicalAllowance = 0f;

    private Float managerialAllowance = 0f;

    private Float leaveTravelAllowance = 0f;

    private Float providentFund = 0f;

    private Float professionalTax = 0f;

    private Float incomeTax = 0f;

    private Float lateEntryDeductions = 0f;

    private Float miscEarnings = 0f;

    private Float miscDeductions = 0f;

    public PayrollDetailsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StaffDTO getStaff() {
        return staff;
    }

    public void setStaff(StaffDTO staff) {
        this.staff = staff;
    }

    public Instant getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Instant deactivationDate) {
        this.deactivationDate = deactivationDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayrollDetailsDTO)) return false;
        PayrollDetailsDTO payrollDetailsDTO = (PayrollDetailsDTO) o;
        return Objects.equals(getId(), payrollDetailsDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "PayrollDetailsDTO{" +
            "id=" + id +
            ", staffId='" + staff.getId() + '\'' +
            '}';
    }
}
