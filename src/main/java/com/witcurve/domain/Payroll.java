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

    @Column(columnDefinition="integer default 0")
    private Integer onTimeDays = 0;

    @Column(columnDefinition="integer default 0")
    private Integer graceDays = 0;

    @Column(columnDefinition="integer default 0")
    private Integer lateDays = 0;

    @Column(columnDefinition="integer default 0")
    private Integer absentDays = 0;

    @Column(columnDefinition="integer default 0")
    private Integer halfDays = 0;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double nonWorkingDays = 0d;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double leavesWithoutPay = 0d;

    @NotNull
    @Column(nullable = false, columnDefinition="Decimal(10,2) default 0.0")
    private Double paidDays = 0d;

    @NotNull
    @Column(nullable = false, columnDefinition="Decimal(10,2) default 0.0")
    private Double payableDays = 0d;

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

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double incomeTax = 0d;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double bonus;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double miscEarnings;

    @Column(columnDefinition="Decimal(10,2) default 0.0")
    private Double miscDeductions;

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

    public Integer getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Integer absentDays) {
        this.absentDays = absentDays;
    }

    public Integer getHalfDays() {
        return halfDays;
    }

    public void setHalfDays(Integer halfDays) {
        this.halfDays = halfDays;
    }

    public Double getNonWorkingDays() {
        return nonWorkingDays;
    }

    public void setNonWorkingDays(Double nonWorkingDays) {
        this.nonWorkingDays = nonWorkingDays;
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
