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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payrollIdSeq")
    @SequenceGenerator(name = "payrollIdSeq", sequenceName="payroll_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private PayrollDetails payrollDetails;

    @Column
    private Float bonus;
    
    @Column
    private Float lossOfPay;
    
    @Column
    private String reasonForLoss;

    @Column
    private Integer checkNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_of_payment")
    private ModeOfPayment modeOfPayment;

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

    public PayrollDetails getPayrollDetails() {
        return payrollDetails;
    }

    public void setPayrollDetails(PayrollDetails payrollDetails) {
        this.payrollDetails = payrollDetails;
    }

    public Float getBonus() {
        return bonus;
    }

    public void setBonus(Float bonus) {
        this.bonus = bonus;
    }

    public Float getLossOfPay() {
        return lossOfPay;
    }

    public void setLossOfPay(Float lossOfPay) {
        this.lossOfPay = lossOfPay;
    }

    public String getReasonForLoss() {
        return reasonForLoss;
    }

    public void setReasonForLoss(String reasonForLoss) {
        this.reasonForLoss = reasonForLoss;
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
