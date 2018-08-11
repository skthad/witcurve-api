package com.witcurve.domain;

import com.witcurve.service.util.InstantTimeConverter;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="exam_details", uniqueConstraints = {
    @UniqueConstraint(name = "exam_date_course_standard_exam_UK",
        columnNames = {"exam_date", "course_id", "class_id", "exam_id"})
})
public class ExamDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examDetailsIdSeq")
    @SequenceGenerator(name = "examDetailsIdSeq", sequenceName="exam_details_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "exam_date", nullable = false)
    @Convert(converter = LocalDateConverter.class)
    private LocalDate examDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "course_id")
    private Course course;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class standard;

    @NotNull
    @Column(name = "start_time",nullable = false)
    @Convert(converter = InstantTimeConverter.class)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    @Convert(converter = InstantTimeConverter.class)
    private Instant endTime;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "exam_id")
    private Exam exam;

    @NotNull
    @Column(name = "syllabus", length = 500, nullable = false)
    private String syllabus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Class getStandard() {
        return standard;
    }

    public void setStandard(Class standard) {
        this.standard = standard;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExamDetails)) return false;
        ExamDetails that = (ExamDetails) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ExamDetails{" +
            "id=" + id +
            ", examDate=" + examDate +
            ", course=" + course +
            ", standard=" + standard +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", exam=" + exam +
            ", syllabus='" + syllabus + '\'' +
            '}';
    }
}
