package com.witcurve.domain;

import com.witcurve.domain.enumeration.QuestionType;
import com.witcurve.service.util.MapToStringConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "survey_question")
public class SurveyQuestion extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @NotNull
    @Column(nullable = false)
    private Boolean required = false;

    @Column
    private Integer minRatingValue;

    @Column
    private Integer maxRatingValue;

    @Column(name = "rating_interval")
    private Integer interval;

    @Column
    private Boolean otherField = false;

    @NotNull
    @Column(nullable = false,name ="question_order")
    private Integer order;

    @Column
    @Convert(converter = MapToStringConverter.class)
    private Map<Integer, String> options;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SurveySection section;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Map<Integer, String> getOptions() { return options; }

    public void setOptions(Map<Integer, String> options) { this.options = options; }

    public SurveySection getSection() {
        return section;
    }

    public void setSection(SurveySection section) {
        this.section = section;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Boolean getOtherField() {
        return otherField;
    }

    public void setOtherField(Boolean otherField) {
        this.otherField = otherField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyQuestion that = (SurveyQuestion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SurveyQuestion{" +
            "id=" + id +
            '}';
    }
}
