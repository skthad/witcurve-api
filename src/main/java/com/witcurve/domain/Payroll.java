package com.witcurve.domain;

import com.witcurve.domain.enumeration.ModeOfPayment;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="payroll")
public class Payroll extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private PayrollCycle payrollCycle;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private PayrollDetails payrollDetails;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_of_payment", nullable = false)
    private ModeOfPayment modeOfPayment;

    @Column
    private String checkNumber;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate initiatedOn;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate closedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayrollCycle getPayrollCycle() {
        return payrollCycle;
    }

    public void setPayrollCycle(PayrollCycle payrollCycle) {
        this.payrollCycle = payrollCycle;
    }

    public PayrollDetails getPayrollDetails() {
        return payrollDetails;
    }

    public void setPayrollDetails(PayrollDetails payrollDetails) {
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

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
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
        if (!(o instanceof Payroll)) return false;
        Payroll that = (Payroll) o;
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
