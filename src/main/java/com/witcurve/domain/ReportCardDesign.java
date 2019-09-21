package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.witcurve.domain.enumeration.CalculationType;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.ReportFieldType;
import com.witcurve.service.util.ListToStringConverter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private Boolean selected;

    @ManyToOne
    private Exam exam;

    @Column(length = 500)
    @Convert(converter = ListToStringConverter.class)
    private List<String> selectedPeriodicTests;

    @Column
    @Enumerated(EnumType.STRING)
    private CalculationType calculationType;

    @Column
    private Integer bestOfValue;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "report_card_design_course",
        joinColumns = {@JoinColumn(name = "report_card_design_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Course> courses = new HashSet<>();

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

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<String> getSelectedPeriodicTests() {
        return selectedPeriodicTests;
    }

    public void setSelectedPeriodicTests(List<String> selectedPeriodicTests) {
        this.selectedPeriodicTests = selectedPeriodicTests;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

    public Integer getBestOfValue() {
        return bestOfValue;
    }

    public void setBestOfValue(Integer bestOfValue) {
        this.bestOfValue = bestOfValue;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
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
