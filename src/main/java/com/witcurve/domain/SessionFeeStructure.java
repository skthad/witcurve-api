package com.witcurve.domain;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.util.LocalDateConverter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "session_fee_structure",  uniqueConstraints = {
    @UniqueConstraint(name = "grade_fee_type_session_id_UK",
        columnNames = {"fee_type_id", "grade", "session_id"})
})
public class SessionFeeStructure extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private FeeDetails feeType;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private AcademicSession session;

    @NotNull
    @Column(nullable = false)
    private Grade grade;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_fee_structure_id")
    private List<SessionFeeDescription> sessionFeeDescriptions;

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

    public List<SessionFeeDescription> getSessionFeeDescriptions() { return sessionFeeDescriptions; }

    public void setSessionFeeDescriptions(List<SessionFeeDescription> sessionFeeDescriptions) { this.sessionFeeDescriptions = sessionFeeDescriptions; }

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
