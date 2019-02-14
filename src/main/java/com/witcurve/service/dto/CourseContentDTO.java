package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class CourseContentDTO extends AbstractAuditingDTO {

    private Long id;

    private String contentName;

    @NotNull
    private Long courseId;

    private Long parentContentId;

    @NotNull
    private Integer contentOrder;

    private String index;

    private String description;

    private List<CourseContentDTO> subTopics;

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

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CourseContentDTO> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(List<CourseContentDTO> subTopics) {
        this.subTopics = subTopics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseContentDTO)) return false;
        CourseContentDTO courseContentDTO = (CourseContentDTO) o;
        return Objects.equals(getId(), courseContentDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "CourseContentDTO{" +
            "id=" + id +
            '}';
    }
}
