package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="course_content", uniqueConstraints = {
    @UniqueConstraint( name= "course_parent_content_order_UK",
        columnNames = {"course_id", "parent_content_id", "content_order"})
})
public class CourseContent extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseContentIdSeq")
    @SequenceGenerator(name = "courseContentIdSeq", sequenceName="course_content_id_seq", allocationSize = 0)
    private Long id;

    @Column
    private String contentName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "parent_content_id")
    private CourseContent parentContent;

    @NotNull
    @Column(name = "content_order", nullable = false)
    private Integer contentOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseContent getParentContent() {
        return parentContent;
    }

    public void setParentContent(CourseContent parentContent) {
        this.parentContent = parentContent;
    }

    public Integer getContentOrder() {
        return contentOrder;
    }

    public void setContentOrder(Integer contentOrder) {
        this.contentOrder = contentOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseContent courseContent = (CourseContent) o;
        return Objects.equals(id, courseContent.id);
    }

    @Override
    public String toString() {
        return "CourseContent{" +
            "id=" + id +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
