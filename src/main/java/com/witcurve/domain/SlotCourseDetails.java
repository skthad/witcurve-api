package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Objects;

@Entity
@Table(name="slot_course_details")
public class SlotCourseDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slotCourseDetailsIdSeq")
    @SequenceGenerator(name = "slotCourseDetailsIdSeq", sequenceName="slot_course_details_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private GeneralSlotDetails gsd;

    @NotNull
    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Course course;

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

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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
        if (!(o instanceof SlotCourseDetails)) return false;
        SlotCourseDetails that = (SlotCourseDetails) o;
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
            ", gsdId=" + gsd.getId() +
            ", dayOfWeek=" + dayOfWeek +
            ", courseId=" + course.getId() +
            '}';
    }
}
