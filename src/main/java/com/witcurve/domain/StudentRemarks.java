package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="student_remarks",  uniqueConstraints = {
    @UniqueConstraint(name = "student_exam_UK",
        columnNames = {"student_id", "exam_id"})
})
public class StudentRemarks extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @NotNull
    @Column(nullable = false, length = 500)
    private String remarks;

    @ManyToOne
    private Exam exam;

    @Column
    private String bindingId;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRemarks that = (StudentRemarks) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentRemarks{" +
            "id=" + id +
            ", student=" + student +
            ", remarks='" + remarks + '\'' +
            ", exam=" + exam +
            ", bindingId='" + bindingId + '\'' +
            '}';
    }
}
