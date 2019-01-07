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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentMarksIdSeq")
    @SequenceGenerator(name = "studentMarksIdSeq", sequenceName="student_marks_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;


    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private Integer marks;

    @ManyToOne
    @JoinColumn(name="examCourseDetails_id")
    private ExamCourseDetails examCourseDetails;

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

    public ExamCourseDetails getExamCourseDetails() {
        return examCourseDetails;
    }

    public void setExamCourseDetails(ExamCourseDetails examCourseDetails) {
        this.examCourseDetails = examCourseDetails;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
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
            ", student=" + student +
            ", event=" + event +
            ", marks=" + marks +
            ", examCourseDetails=" + examCourseDetails +
            '}';
    }
}
