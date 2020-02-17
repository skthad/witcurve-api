package com.witcurve.service.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class SurveySectionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Length(max = 50, message = "The field name must be less than 50 characters")
    private String name;

    @Length(max = 1000, message = "The field description must be less than 1000 characters")
    private String description;

    @NotNull
    private Long formId;

    @NotNull
    private Integer order;

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

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Integer getOrder() { return order; }

    public void setOrder(Integer order) { this.order = order; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveySectionDTO that = (SurveySectionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SurveySectionDTO{" +
            "id=" + id +
            '}';
    }
}
