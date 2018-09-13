package com.witcurve.domain;

import org.mapstruct.Mapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="student_class", uniqueConstraints = {
    @UniqueConstraint(name = "student_standard_UK",
        columnNames = {"student_id", "class_id"})
})
public class StudentClass extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "student_id")
    private Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "class_id")
    private Class standard;

    @NotNull
    @Column(name = "roll_no", nullable = false)
    private String rollNo;

    // add roll no.

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

    public Class getStandard() {
        return standard;
    }

    public void setStandard(Class standard) {
        this.standard = standard;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentClass)) return false;
        StudentClass that = (StudentClass) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "StudentClass{" +
            "id=" + id +
            ", student=" + student +
            ", standard=" + standard +
            ", rollNo='" + rollNo + '\'' +
            '}';
    }
}
