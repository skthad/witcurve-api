package com.witcurve.domain;

import com.witcurve.domain.enumeration.ReportFieldType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "report_card_design")
public class ReportCardDesign extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(length = 5)
    private String shortForm;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportFieldType fieldType;

    @Column(name = "report_order")
    private Integer order;

    @Column(name = "marks", precision=10, scale=2)
    private Double marks;

    @Column
    private Boolean showGradesOnly;

    @Column
    private Boolean showMarksOnly;

    @Column
    private Boolean selected;

    @ManyToOne
    private Exam exam;

    @Column
    private String bindingId;

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

    public String getShortForm() {
        return shortForm;
    }

    public void setShortForm(String shortForm) {
        this.shortForm = shortForm;
    }

    public ReportFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(ReportFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }

    public Boolean getShowGradesOnly() {
        return showGradesOnly;
    }

    public void setShowGradesOnly(Boolean showGradesOnly) {
        this.showGradesOnly = showGradesOnly;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getShowMarksOnly() {
        return showMarksOnly;
    }

    public void setShowMarksOnly(Boolean showMarksOnly) {
        this.showMarksOnly = showMarksOnly;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportCardDesign that = (ReportCardDesign) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReportCardDesign{" +
            "id=" + id +
            '}';
    }
}
