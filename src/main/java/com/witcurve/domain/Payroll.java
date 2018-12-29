package com.witcurve.domain;

import com.witcurve.domain.enumeration.ModeOfPayment;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

@Entity
@Table(name="payroll", uniqueConstraints = {
    @UniqueConstraint(name = "payroll_month_year_staff_UK",
        columnNames = {"month", "year", "staff_id"})
})
public class Payroll extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payrollIdSeq")
    @SequenceGenerator(name = "payrollIdSeq", sequenceName="payroll_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private PayrollDetails payrollDetails;

    @Column
    private Integer checkNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "mode_of_payment", nullable = false)
    private ModeOfPayment modeOfPayment;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate initiatedOn;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate closedOn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private Month month;

    @NotNull
    @Min(value = 1900)
    @Max(value = 2099)
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate cycleStart;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate cycleEnd;

    @NotNull
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PayrollDetails getPayrollDetails() {
        return payrollDetails;
    }

    public void setPayrollDetails(PayrollDetails payrollDetails) {
        this.payrollDetails = payrollDetails;
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

    public LocalDate getCycleStart() {
        return cycleStart;
    }

    public void setCycleStart(LocalDate cycleStart) {
        this.cycleStart = cycleStart;
    }

    public LocalDate getCycleEnd() {
        return cycleEnd;
    }

    public void setCycleEnd(LocalDate cycleEnd) {
        this.cycleEnd = cycleEnd;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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
