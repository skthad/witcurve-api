package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="time_table_unit", uniqueConstraints = {
    @UniqueConstraint(name = "course_teacher_slot_day_UK",
        columnNames = {"course_teacher_id", "slot_id", "day_of_week"})
})
public class TimeTableUnit extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeTableUnitIdSeq")
    @SequenceGenerator(name = "timeTableUnitIdSeq", sequenceName="time_table_unit_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private CourseTeacher courseTeacher;

    @NotNull
    @JoinColumn(nullable = false)
    @ManyToOne
    private Slot slot;

    @NotNull
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseTeacher getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(CourseTeacher courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTableUnit timeTableUnit = (TimeTableUnit) o;
        return Objects.equals(id, timeTableUnit.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TimeTableUnit{" +
            "id=" + id +
            ", courseTeacher=" + courseTeacher +
            ", slot=" + slot +
            ", dayOfWeek=" + dayOfWeek +
            '}';
    }
}
