package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="note")
public class Note extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noteIdSeq")
    @SequenceGenerator(name = "noteIdSeq", sequenceName="note_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "posted_date", nullable = false)
    private LocalDate postedDate;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private CourseTeacher courseTeacher;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false, name = "student_id")
    private Student student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return Objects.equals(getId(), note.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", postedDate=" + postedDate +
            ", courseTeacher=" + courseTeacher +
            ", student=" + student +
            '}';
    }
}
