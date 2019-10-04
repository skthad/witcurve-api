package com.witcurve.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.witcurve.domain.enumeration.Grade;
import com.witcurve.service.util.MapToStringConverter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "report_card", uniqueConstraints = {
    @UniqueConstraint(name = "grade_exam_UK",
        columnNames = {"grade", "exam_id"})
})
public class ReportCard extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Exam exam;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "report_card_scholastic_course",
        joinColumns = {@JoinColumn(name = "report_card_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Course> scholasticCourses = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "report_card_non_scholastic_course",
        joinColumns = {@JoinColumn(name = "report_card_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "id")})
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Course> nonScholasticCourses = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="report_card_id")
    @OrderBy("non_scholastic_order asc")
    private List<NonScholasticReportDetails> nonScholasticReportDetails;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="report_card_id")
    @OrderBy("scholastic_order asc")
    private List<ScholasticReportDetails> scholasticDetails;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showAttributes = false;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showRemarks=false;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showOverallMarks=false;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showOverallGrade=false;

    @NotNull
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean showAttendance=false;


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

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Set<Course> getScholasticCourses() {
        return scholasticCourses;
    }

    public void setScholasticCourses(Set<Course> scholasticCourses) {
        this.scholasticCourses = scholasticCourses;
    }

    public Set<Course> getNonScholasticCourses() {
        return nonScholasticCourses;
    }

    public void setNonScholasticCourses(Set<Course> nonScholasticCourses) {
        this.nonScholasticCourses = nonScholasticCourses;
    }

    public List<NonScholasticReportDetails> getNonScholasticReportDetails() {
        return nonScholasticReportDetails;
    }

    public void setNonScholasticReportDetails(List<NonScholasticReportDetails> nonScholasticReportDetails) {
        this.nonScholasticReportDetails = nonScholasticReportDetails;
    }

    public List<ScholasticReportDetails> getScholasticDetails() {
        return scholasticDetails;
    }

    public void setScholasticDetails(List<ScholasticReportDetails> scholasticDetails) {
        this.scholasticDetails = scholasticDetails;
    }

    public Boolean getShowAttributes() {
        return showAttributes;
    }

    public void setShowAttributes(Boolean showAttributes) {
        this.showAttributes = showAttributes;
    }

    public Boolean getShowRemarks() {
        return showRemarks;
    }

    public void setShowRemarks(Boolean showRemarks) {
        this.showRemarks = showRemarks;
    }

    public Boolean getShowOverallMarks() {
        return showOverallMarks;
    }

    public void setShowOverallMarks(Boolean showOverallMarks) {
        this.showOverallMarks = showOverallMarks;
    }

    public Boolean getShowOverallGrade() {
        return showOverallGrade;
    }

    public void setShowOverallGrade(Boolean showOverallGrade) {
        this.showOverallGrade = showOverallGrade;
    }

    public Boolean getShowAttendance() {
        return showAttendance;
    }

    public void setShowAttendance(Boolean showAttendance) {
        this.showAttendance = showAttendance;
    }

    @Override
    public String toString() {
        return "ReportCard{" +
            "id=" + id +
            '}';
    }
}
