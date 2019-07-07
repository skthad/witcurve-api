package com.witcurve.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseTrackDTO {

    CourseDTO courseDTO;

    List<CourseContentDTO> content;

    Map<String, Map<String, Long>> sectionTopicCountMap;

    public CourseTrackDTO(CourseDTO courseDTO, List<CourseContentDTO> content, Map<String, Map<String, Long>> sectionTopicCountMap) {
        this.courseDTO = courseDTO;
        this.content = content;
        this.sectionTopicCountMap = sectionTopicCountMap;
    }

    public CourseDTO getCourseDTO() {
        return courseDTO;
    }

    public void setCourseDTO(CourseDTO courseDTO) {
        this.courseDTO = courseDTO;
    }

    public List<CourseContentDTO> getContent() {
        return content;
    }

    public void setContent(List<CourseContentDTO> content) {
        this.content = content;
    }

    public Map<String, Map<String, Long>> getSectionTopicCountMap() {
        return sectionTopicCountMap;
    }

    public void setSectionTopicCountMap(Map<String, Map<String, Long>> sectionTopicCountMap) {
        this.sectionTopicCountMap = sectionTopicCountMap;
    }
}
