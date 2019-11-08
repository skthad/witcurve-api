package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.QuestionType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class SurveyQuestionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private QuestionType type;

    @NotNull
    private Boolean required = false;

    @NotNull
    private Integer order;

    private Integer minRatingValue;

    private Integer maxRatingValue;

    private Map<Long, String> options;

    private Long sectionId;

    private Boolean otherField = false;

    private Integer interval;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getMinRatingValue() {
        return minRatingValue;
    }

    public void setMinRatingValue(Integer minRatingValue) {
        this.minRatingValue = minRatingValue;
    }

    public Integer getMaxRatingValue() {
        return maxRatingValue;
    }

    public void setMaxRatingValue(Integer maxRatingValue) {
        this.maxRatingValue = maxRatingValue;
    }

    public Map<Long, String> getOptions() {
        return options;
    }

    public void setOptions(Map<Long, String> options) {
        this.options = options;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Boolean getOtherField() { return otherField; }

    public void setOtherField(Boolean otherField) { this.otherField = otherField; }

    public Integer getInterval() { return interval; }

    public void setInterval(Integer interval) { this.interval = interval; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyQuestionDTO that = (SurveyQuestionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SurveyQuestionDTO{" +
            "id=" + id +
            '}';
    }
}
