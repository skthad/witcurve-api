package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class AttributeValueDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long studentId;

    @NotNull
    private Long attributeId;

    @NotNull
    private String value;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getStudentId() { return studentId; }

    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getAttributeId() { return attributeId; }

    public void setAttributeId(Long attributeId) { this.attributeId = attributeId; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeValueDTO)) return false;
        AttributeValueDTO that = (AttributeValueDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
            "id=" + id +
            '}';
    }
}

