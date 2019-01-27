package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CourseContentDTO extends AbstractAuditingDTO {

    private Long id;

    private String contentName;

    @NotNull
    private Long courseId;

    private Long parentContentId;

    @NotNull
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getParentContentId() {
        return parentContentId;
    }

    public void setParentContentId(Long parentContentId) {
        this.parentContentId = parentContentId;
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
        if (!(o instanceof CourseContentDTO)) return false;
        CourseContentDTO courseDTO = (CourseContentDTO) o;
        return Objects.equals(getId(), courseDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + id +
            '}';
    }
}
