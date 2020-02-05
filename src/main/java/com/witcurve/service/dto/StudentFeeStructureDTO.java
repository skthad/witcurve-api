package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class StudentFeeStructureDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long sessionId;

    @NotNull
    private Long studentId;

    @NotNull
    private Long selectedSessionId;

    @NotNull
    private List<StudentFeeTypeDTO> studentFeeTypes;

    private Double totalAmount;

    private Double totalOneTimeDiscount;

    private Double paidAmount;

    private Double paidPenalty;

    private Double dueAmount;

    @NotNull
    private Boolean required = false;

    private String admissionId;

    private String className;

    private LocalDate dateOfJoining;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSelectedSessionId() {
        return selectedSessionId;
    }

    public void setSelectedSessionId(Long selectedSessionId) {
        this.selectedSessionId = selectedSessionId;
    }

    public List<StudentFeeTypeDTO> getStudentFeeTypes() { return studentFeeTypes; }

    public void setStudentFeeTypes(List<StudentFeeTypeDTO> studentFeeTypes) { this.studentFeeTypes = studentFeeTypes; }

    public Double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getTotalOneTimeDiscount() { return totalOneTimeDiscount; }

    public void setTotalOneTimeDiscount(Double totalOneTimeDiscount) { this.totalOneTimeDiscount = totalOneTimeDiscount; }

    public Boolean getRequired() { return required; }

    public void setRequired(Boolean required) { this.required = required; }

    public Double getPaidAmount() { return paidAmount; }

    public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }

    public Double getPaidPenalty() { return paidPenalty; }

    public void setPaidPenalty(Double paidPenalty) { this.paidPenalty = paidPenalty; }

    public Double getDueAmount() { return dueAmount; }

    public void setDueAmount(Double dueAmount) { this.dueAmount = dueAmount; }

    public String getAdmissionId() { return admissionId; }

    public void setAdmissionId(String admissionId) { this.admissionId = admissionId; }

    public String getClassName() { return className; }

    public void setClassName(String className) { this.className = className; }

    public LocalDate getDateOfJoining() { return dateOfJoining; }

    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentFeeStructureDTO that = (StudentFeeStructureDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentFeeStructureDTO{" +
            "id=" + id +
            '}';
    }
}
