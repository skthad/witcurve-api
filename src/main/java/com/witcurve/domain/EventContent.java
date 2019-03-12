package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="event_content", uniqueConstraints = {
    @UniqueConstraint( name= "event_content_id_UK",
        columnNames = {"event_id", "course_content_id"})
})
public class EventContent extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "eventContentIdSeq", sequenceName="event_content_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_content_id", nullable = false)
    private CourseContent courseContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public CourseContent getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(CourseContent courseContent) {
        this.courseContent = courseContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventContent eventContent = (EventContent) o;
        return Objects.equals(id, eventContent.id);
    }

    @Override
    public String toString() {
        return "EventContent{" +
            "id=" + id +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
