package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;
import com.witcurve.domain.enumeration.StudentDetails;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ReportCardDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Grade grade;

    @NotNull
    private Long examId;

    private Set<CourseDTO> scholasticCourses;

    private Set<CourseDTO> nonScholasticCourses;

    private List<ScholasticReportDetailsDTO> scholasticDetails;

    private List<NonScholasticReportDetailsDTO> nonScholasticDetails;

    private List<StudentDetails> studentDetails;

    @NotNull
    private Boolean showAttributes = false;

    @NotNull
    private Boolean showRemarks = false;

    @NotNull
    private Boolean showOverallMarks = false;

    @NotNull
    private Boolean showOverallGrade = false;

    @NotNull
    private Boolean showAttendance=false;

    @NotNull
    private Double pageTop = 1.00;

    @NotNull
    private Double pageBottom = 1.00;

    @NotNull
    private Double pageLeft = 1.50;

    @NotNull
    private Double pageRight = 1.50;

    @NotNull
    private Double tableGap = 0.50;

    private String note;

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

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Set<CourseDTO> getScholasticCourses() {
        return scholasticCourses;
    }

    public void setScholasticCourses(Set<CourseDTO> scholasticCourses) {
        this.scholasticCourses = scholasticCourses;
    }

    public Set<CourseDTO> getNonScholasticCourses() {
        return nonScholasticCourses;
    }

    public void setNonScholasticCourses(Set<CourseDTO> nonScholasticCourses) {
        this.nonScholasticCourses = nonScholasticCourses;
    }

    public List<ScholasticReportDetailsDTO> getScholasticDetails() {
        return scholasticDetails;
    }

    public void setScholasticDetails(List<ScholasticReportDetailsDTO> scholasticDetails) {
        this.scholasticDetails = scholasticDetails;
    }

    public List<NonScholasticReportDetailsDTO> getNonScholasticDetails() {
        return nonScholasticDetails;
    }

    public void setNonScholasticDetails(List<NonScholasticReportDetailsDTO> nonScholasticDetails) {
        this.nonScholasticDetails = nonScholasticDetails;
    }

    public List<StudentDetails> getStudentDetails() {
        return studentDetails;
    }

    public void setStudentDetails(List<StudentDetails> studentDetails) {
        this.studentDetails = studentDetails;
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

    public Double getPageTop() {
        return pageTop;
    }

    public void setPageTop(Double pageTop) {
        this.pageTop = pageTop;
    }

    public Double getPageBottom() {
        return pageBottom;
    }

    public void setPageBottom(Double pageBottom) {
        this.pageBottom = pageBottom;
    }

    public Double getPageLeft() {
        return pageLeft;
    }

    public void setPageLeft(Double pageLeft) {
        this.pageLeft = pageLeft;
    }

    public Double getPageRight() {
        return pageRight;
    }

    public void setPageRight(Double pageRight) {
        this.pageRight = pageRight;
    }

    public Double getTableGap() {
        return tableGap;
    }

    public void setTableGap(Double tableGap) {
        this.tableGap = tableGap;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportCardDTO that = (ReportCardDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ReportCardDTO{" +
            "id=" + id +
            '}';
    }
}
