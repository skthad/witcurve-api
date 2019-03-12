package com.witcurve.domain;

import com.witcurve.domain.enumeration.Grade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="staff_eligibility", uniqueConstraints = {
    @UniqueConstraint(name = "staff_eligibility_subject_staff_grade_UK",
        columnNames = {"master_subject_name", "staff_id", "grade"})
})
public class StaffEligibility extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "staffEligibilityIdSeq", sequenceName="staff_eligibility_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Staff staff;

    @NotNull
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private MasterSubject masterSubject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public MasterSubject getMasterSubject() {
        return masterSubject;
    }

    public void setMasterSubject(MasterSubject masterSubject) {
        this.masterSubject = masterSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StaffEligibility)) return false;
        StaffEligibility institute = (StaffEligibility) o;
        return Objects.equals(getId(), institute.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "StaffEligibility{" +
            "id=" + id +
            ", staffId='" + staff.getId()+ '\'' +
            ", grade='" + grade+ '\'' +
            ", masterSubject='" + masterSubject.getName()+ '\'' +
            '}';
    }
}
