package com.witcurve.domain;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="exam", uniqueConstraints = {
    @UniqueConstraint(name = "exam_name_academic_session_UK",
        columnNames = {"name", "academic_session_id"})
})
public class Exam extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examIdSeq")
    @SequenceGenerator(name = "examIdSeq", sequenceName="exam_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "start_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate endDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name ="academic_session_id", nullable = false)
    private AcademicSession academicSession;

    @NotNull
    @Column(name = "grade", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public AcademicSession getAcademicSession() {
        return academicSession;
    }

    public void setAcademicSession(AcademicSession academicSession) {
        this.academicSession = academicSession;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return Objects.equals(getId(), exam.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Exam{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", academicSession=" + academicSession +
            '}';
    }
}
