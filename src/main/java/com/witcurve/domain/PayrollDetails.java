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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Staff staff;

    @Column
    @Convert(converter = InstantTimeConverter.class)
    private Instant deactivationDate;

    @NotNull
    @Column(nullable = false, columnDefinition="Decimal(10,2) default 0.0")
    private Double basicSalary = 0d;
    
    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double houseRentAllowance = 0d;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double conveyanceAllowance = 0d;
    
    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double medicalAllowance = 0d;
    
    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double managerialAllowance = 0d;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double leaveTravelAllowance = 0d;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double providentFund = 0d;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double professionalTax = 0d;

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
            '}';
    }
}
