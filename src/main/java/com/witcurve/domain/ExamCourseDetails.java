package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="exam_course_details", uniqueConstraints = {
    @UniqueConstraint(name = "ecd_slot_date_course_UK",
        columnNames = {"gsd_id", "date", "course_id"})
})
public class ExamCourseDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private GeneralSlotDetails gsd;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Course course;

    @NotNull
    @Column(name = "full_marks", nullable = false)
    private Integer fullMarks;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="exam_course_details_id", insertable = false, updatable = false)
    @Size(min=1, max=1)
    private Set<EventContent> eventContents;

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="exam_course_details_id", insertable = false, updatable = false)
    @Size(min=1, max=1)
    private Set<StudentMarks> studentMarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public GeneralSlotDetails getGsd() {
        return gsd;
    }

    public void setGsd(GeneralSlotDetails gsd) {
        this.gsd = gsd;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getFullMarks() {
        return fullMarks;
    }

    public void setFullMarks(Integer fullMarks) {
        this.fullMarks = fullMarks;
    }

    public Set<EventContent> getEventContents() {
        return eventContents;
    }

    public void setEventContents(Set<EventContent> eventContents) {
        this.eventContents = eventContents;
    }

    public Set<StudentMarks> getStudentMarks() {
        return studentMarks;
    }

    public void setStudentMarks(Set<StudentMarks> studentMarks) {
        this.studentMarks = studentMarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExamCourseDetails)) return false;
        ExamCourseDetails that = (ExamCourseDetails) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ExamCourseDetails{" +
            "id=" + id +
            '}';
    }
}
