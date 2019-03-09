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
    private PayrollDetailsDTO payrollDetails;

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

    public PayrollDetailsDTO getPayrollDetails() {
        return payrollDetails;
    }

    public void setPayrollDetails(PayrollDetailsDTO payrollDetails) {
        this.payrollDetails = payrollDetails;
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
