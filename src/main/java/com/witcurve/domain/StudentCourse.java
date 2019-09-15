package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="student_course", uniqueConstraints = {
    @UniqueConstraint(name = "student_course_unique_UK",
    columnNames = {"student_standard_id", "course_id"})
})
public class StudentCourse extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_standard_id")
    private StudentStandard studentStandard;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentStandard getStudentStandard() {
        return studentStandard;
    }

    public void setStudentStandard(StudentStandard studentStandard) {
        this.studentStandard = studentStandard;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourse eventContent = (StudentCourse) o;
        return Objects.equals(id, eventContent.id);
    }

    @Override
    public String toString() {
        return "EventContent{" +
            "id=" + id +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
