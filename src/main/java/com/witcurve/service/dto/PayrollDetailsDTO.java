package com.witcurve.service.dto;

import java.time.LocalDate;
import java.util.Objects;

public class PayrollDetailsDTO extends AbstractAuditingDTO {

    private Long id;

    private StaffDTO staff;

    private LocalDate deactivationDate;

    private Float basicSalary;

    private Float houseRengAllowance;

    private Float conveyanceAllowance;

    private Float medicalAllowance;

    private Float managerialAllowance;

    private Float leaveTravelAlowance;

    private Float providendFund;

    private Float professionalText;

    private Float incomeTax;

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

    public LocalDate getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(LocalDate deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public Float getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public Float getHouseRengAllowance() {
        return houseRengAllowance;
    }

    public void setHouseRengAllowance(Float houseRengAllowance) {
        this.houseRengAllowance = houseRengAllowance;
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

    public Float getLeaveTravelAlowance() {
        return leaveTravelAlowance;
    }

    public void setLeaveTravelAlowance(Float leaveTravelAlowance) {
        this.leaveTravelAlowance = leaveTravelAlowance;
    }

    public Float getProvidendFund() {
        return providendFund;
    }

    public void setProvidendFund(Float providendFund) {
        this.providendFund = providendFund;
    }

    public Float getProfessionalText() {
        return professionalText;
    }

    public void setProfessionalText(Float professionalText) {
        this.professionalText = professionalText;
    }

    public Float getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(Float incomeTax) {
        this.incomeTax = incomeTax;
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
