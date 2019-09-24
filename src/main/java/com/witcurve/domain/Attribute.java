package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * An attribute entity
 */
@Entity
@Table(name = "attribute")
public class Attribute extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 50)
    private String title;

    @NotNull
    @Column(nullable = false, name = "attribute_column", length = 50)
    private String column;

    @NotNull
    @Column(nullable = false)
    private String field;

    @NotNull
    @Column(nullable = false)
    private Boolean highlight = false;

    @NotNull
    @Column(nullable = false)
    private Integer titleOrder;

    @NotNull
    @Column(nullable = false)
    private Integer columnOrder;

    @NotNull
    @Column(nullable = false)
    private Integer fieldOrder;

    @ManyToOne
    private ReportCardDesign reportCardDesign;

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

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public ReportCardDesign getReportCardDesign() {
        return reportCardDesign;
    }

    public void setReportCardDesign(ReportCardDesign reportCardDesign) {
        this.reportCardDesign = reportCardDesign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return Objects.equals(id, attribute.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + id +
            '}';
    }
}
