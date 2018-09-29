package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="course_teacher", uniqueConstraints = {
    @UniqueConstraint(name = "course_teacher_class_UK",
        columnNames = {"course_id", "teacher_id", "standard_id"})
})
public class CourseTeacher extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseTeacherIdSeq")
    @SequenceGenerator(name = "courseTeacherIdSeq", sequenceName="course_teacher_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Course course;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Staff teacher;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "standard_id",nullable = false)
    private Standard standard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Staff getTeacher() {
        return teacher;
    }

    public void setTeacher(Staff teacher) {
        this.teacher = teacher;
    }

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseTeacher that = (CourseTeacher) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CourseTeacher{" +
            "id=" + id +
            ", course=" + course +
            ", teacher=" + teacher +
            ", standard=" + standard +
            '}';
    }
}
