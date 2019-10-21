package com.witcurve.domain;

import com.sun.istack.NotNull;
import com.witcurve.domain.enumeration.Grade;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "session_fee_structure")
public class SessionFeeStructure extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "fee_details_id", nullable = false)
    private FeeDetails feeType;

    @NotNull
    @JoinColumn(name = "fee_details_id", nullable = false)
    private FeeDetails FeeDescription;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "academic_session_id", nullable = false)
    private AcademicSession session;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @NotNull
    @Column(nullable = false)
    private Double amount;

    @Column
    private LocalDate dueDate;

    @Column
    private Double penalty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeeDetails getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeDetails feeType) {
        this.feeType = feeType;
    }

    public FeeDetails getFeeDescription() {
        return FeeDescription;
    }

    public void setFeeDescription(FeeDetails feeDescription) {
        FeeDescription = feeDescription;
    }

    public AcademicSession getSession() {
        return session;
    }

    public void setSession(AcademicSession session) {
        this.session = session;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getPenalty() {
        return penalty;
    }

    public void setPenalty(Double penalty) {
        this.penalty = penalty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionFeeStructure that = (SessionFeeStructure) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SessionFeeStructure{" +
            "id=" + id +
            '}';
    }
}
