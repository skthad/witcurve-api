package com.witcurve.domain;

import com.witcurve.service.util.InstantTimeConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name="payroll_details", uniqueConstraints = {
    @UniqueConstraint(name = "staff_payroll_cycle_UK",
        columnNames = {"staff_id", "payroll_cycle_id"})
})
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

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private PayrollCycle payrollCycle;

    @Column
    @Convert(converter = InstantTimeConverter.class)
    private Instant deactivationDate;

    @NotNull
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer paidDays = 0;

    @NotNull
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer totalDays = 0;

    @NotNull
    @Column(nullable = false, columnDefinition = "int default 0")
    private Float basicSalary = 0f;
    
    @Column
    private Float houseRentAllowance;

    @Column
    private Float conveyanceAllowance;
    
    @Column
    private Float medicalAllowance;
    
    @Column
    private Float managerialAllowance;

    @Column
    private Float leaveTravelAllowance;

    @Column
    private Float providentFund;

    @Column
    private Float professionalTax;
    
    @Column
    private Float incomeTax;

    @Column
    private Float lateEntryDeductions;

    @Column
    private Float bonus;

    @Column
    private Float miscEarnings;

    @Column
    private Float miscDeductions;

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

    public PayrollCycle getPayrollCycle() {
        return payrollCycle;
    }

    public void setPayrollCycle(PayrollCycle payrollCycle) {
        this.payrollCycle = payrollCycle;
    }

    public Instant getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Instant deactivationDate) {
        this.deactivationDate = deactivationDate;
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
