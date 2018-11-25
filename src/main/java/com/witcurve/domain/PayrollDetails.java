package com.witcurve.domain;

import com.witcurve.service.util.InstantTimeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name="payroll_details")
public class PayrollDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payrollDetailsIdSeq")
    @SequenceGenerator(name = "payrollDetailsIdSeq", sequenceName="payroll_details_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Staff staff;

    @Column
    @Convert(converter = InstantTimeConverter.class)
    private Instant deactivationDate;

    @Column
    private Float basicSalary;
    
    @Column
    private Float houseRentAllowance;

    @Column
    private Float conveyanceAllowance;
    
    @Column
    private Float medicalAllowance;
    
    @Column
    private Float managerialAllowance;

    @Column
    private Float leaveTravelAlowance;

    @Column
    private Float providendFund;

    @Column
    private Float professionalTax;
    
    @Column
    private Float incomeTax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayrollDetails)) return false;
        PayrollDetails that = (PayrollDetails) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "PayrollDetails{" +
            "id=" + id +
            ", staffId='" + staff.getId() + '\'' +
            '}';
    }
}
