package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class StudentFeeTypeDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long feeTypeId;

    private Double penalty;

    private LocalDate dueDate;

    @NotNull
    private List<StudentFeeDescriptionDTO> studentFeeDescriptions;

    private Double amount;

    private String feeTypeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Long feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public List<StudentFeeDescriptionDTO> getStudentFeeDescriptions() {
        return studentFeeDescriptions;
    }

    public void setStudentFeeDescriptions(List<StudentFeeDescriptionDTO> studentFeeDescriptions) { this.studentFeeDescriptions = studentFeeDescriptions; }

    public Double getAmount() { return amount; }

    public void setAmount(Double amount) { this.amount = amount; }

    public String getFeeTypeName() { return feeTypeName; }

    public void setFeeTypeName(String feeTypeName) { this.feeTypeName = feeTypeName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentFeeTypeDTO that = (StudentFeeTypeDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentFeeTypeDTO{" +
            "id=" + id +
            '}';
    }
}
