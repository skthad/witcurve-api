package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="student_marks")
public class StudentMarks extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    @Column(name = "marks", nullable = false, precision=10, scale=2)
    private Double marks;

    @ManyToOne
    private ReportCardDesign reportCardDesign;

    @ManyToOne
    private Course course;

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public ReportCardDesign getReportCardDesign() {
        return reportCardDesign;
    }

    public void setReportCardDesign(ReportCardDesign reportCardDesign) {
        this.reportCardDesign = reportCardDesign;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentMarks)) return false;
        StudentMarks institute = (StudentMarks) o;
        return Objects.equals(getId(), institute.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "StudentMarks{" +
            "id=" + id +
            '}';
    }
}
