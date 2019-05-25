package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="event_content")
public class EventContent extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "exam_course_details_id")
    private ExamCourseDetails ecd;

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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ExamCourseDetails getEcd() {
        return ecd;
    }

    public void setEcd(ExamCourseDetails ecd) {
        this.ecd = ecd;
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
