package com.witcurve.domain;

import com.witcurve.domain.enumeration.Grade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="class", uniqueConstraints = {
    @UniqueConstraint(name = "grade_section_teacher_term_UK",
        columnNames = {"grade", "section", "term_id", "class_teacher_id"})
})
public class Class extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classIdSeq")
    @SequenceGenerator(name = "classIdSeq", sequenceName="class_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Column(length = 50)
    private String section;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Staff classTeacher;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Term term;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Staff getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(Staff classTeacher) {
        this.classTeacher = classTeacher;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class aClass = (Class) o;
        return Objects.equals(id, aClass.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Class{" +
            "id=" + id +
            ", grade=" + grade +
            ", section='" + section + '\'' +
            ", classTeacher=" + classTeacher +
            ", term=" + term +
            '}';
    }
}
