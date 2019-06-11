package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Objects;

@Entity
@Table(name="slot_course_details", uniqueConstraints = {
    @UniqueConstraint(name = "scd_slot_day_UK",
        columnNames = {"gsd_id", "day_of_week"})
})
public class SlotCourseDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private GeneralSlotDetails gsd;

    @NotNull
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private CourseTeacher courseTeacher;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted = false;

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

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
            '}';
    }
}
