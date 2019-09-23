package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class AttributeDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String title;

    @NotNull
    @Size(max = 50)
    private String column;

    @NotNull
    @Size(max = 255)
    private String field;

    @NotNull
    private Boolean highlight = false;

    private Long rcdId;

    @NotNull
    private Integer titleOrder;

    @NotNull
    private Integer columnOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Boolean getHighlight() {
        return highlight;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }

    public Long getRcdId() {
        return rcdId;
    }

    public void setRcdId(Long rcdId) {
        this.rcdId = rcdId;
    }

    public Integer getTitleOrder() {
        return titleOrder;
    }

    public void setTitleOrder(Integer titleOrder) {
        this.titleOrder = titleOrder;
    }

    public Integer getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(Integer columnOrder) {
        this.columnOrder = columnOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeDTO that = (AttributeDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AttributeDTO{" +
            "id=" + id +
            '}';
    }
}
