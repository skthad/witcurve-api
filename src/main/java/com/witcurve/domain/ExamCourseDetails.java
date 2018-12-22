package com.witcurve.domain;

import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="exam_course_details", uniqueConstraints = {
    @UniqueConstraint(name = "ecd_slot_day_UK",
        columnNames = {"gsd_id", "date"})
})
public class ExamCourseDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examCourseDetailsIdSeq")
    @SequenceGenerator(name = "examCourseDetailsIdSeq", sequenceName="exam_course_details_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private GeneralSlotDetails gsd;

    @NotNull
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private CourseTeacher courseTeacher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeneralSlotDetails getGsd() {
        return gsd;
    }

    public void setGsd(GeneralSlotDetails gsd) {
        this.gsd = gsd;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
        this.courseTeacher = courseTeacher;
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
        return "SlotCourseDetails{" +
            "id=" + id +
            ", gsd=" + gsd +
            ", date=" + date +
            ", courseTeacher=" + courseTeacher +
            '}';
    }
}
