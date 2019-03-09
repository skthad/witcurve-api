package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="student_standard", uniqueConstraints = {
    @UniqueConstraint(name = "student_standard_UK",
        columnNames = {"student_id", "standard_id"})
})
public class StudentStandard extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentStandardIdSeq")
    @SequenceGenerator(name = "studentStandardIdSeq", sequenceName="student_standard_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "student_id")
    private Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "standard_id")
    private Standard standard;

    @NotNull
    @Column(name = "roll_no", nullable = false)
    private String rollNo;

    @NotNull
    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentStandard)) return false;
        StudentStandard that = (StudentStandard) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "StudentStandard{" +
            "id=" + id +
            '}';
    }
}
