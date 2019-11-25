package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "student_fee_structure", uniqueConstraints = {
    @UniqueConstraint(name = "student_session_id_UK",
        columnNames = {"student_id", "session_id"})
})
public class StudentFeeStructure extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private AcademicSession session;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private AcademicSession selectedSession;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_fee_structure_id")
    private List<StudentFeeType> studentFeeTypes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AcademicSession getSession() {
        return session;
    }

    public void setSession(AcademicSession session) {
        this.session = session;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AcademicSession getSelectedSession() {
        return selectedSession;
    }

    public void setSelectedSession(AcademicSession selectedSession) {
        this.selectedSession = selectedSession;
    }

    public List<StudentFeeType> getStudentFeeTypes() {
        return studentFeeTypes;
    }

    public void setStudentFeeTypes(List<StudentFeeType> studentFeeTypes) {
        this.studentFeeTypes = studentFeeTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentFeeStructure that = (StudentFeeStructure) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudentFeeStructure{" +
            "id=" + id +
            '}';
    }
}
