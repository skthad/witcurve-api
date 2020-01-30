package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "student_fee_type", uniqueConstraints = {
    @UniqueConstraint(name = "student_fee_structure_id_fee_type_id_UK",
        columnNames = {"student_fee_structure_id", "fee_type_id"})
})
public class StudentFeeType extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private FeeDetails feeType;

    @Column
    @Min(value = 0L, message = "penalty amount must be positive")
    private Double penalty;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate dueDate;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_fee_type_id",nullable = false)
    private List<StudentFeeDescription> studentFeeDescriptions;

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

    public List<StudentFeeDescription> getStudentFeeDescriptions() {
        return studentFeeDescriptions;
    }

    public void setStudentFeeDescriptions(List<StudentFeeDescription> studentFeeDescriptions) { this.studentFeeDescriptions = studentFeeDescriptions; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentFeeType that = (StudentFeeType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentFeeType{" +
            "id=" + id +
            '}';
    }
}
