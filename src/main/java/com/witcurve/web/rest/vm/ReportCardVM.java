package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;

public class ReportCardVM {

    @NotNull
    private String studentName;

    @NotNull
    private String standard;

    @NotNull
    private String admissionId;

    @NotNull
    private String rollNo;

    @NotNull
        private String attendance;

    @NotNull
    private String examName;

    @NotNull
    private String schoolName;

    @NotNull
    private Boolean showHeader = true;

    @NotNull
    private String logoLink;

    @NotNull
    private String remarks;

    @NotNull
    private String[] subjectsArray;

    @NotNull
    private Map<String, String> colorForGrades;

    @NotNull
    private Map<String, String> definingGrades;

    @NotNull
    private Map<String, Object> marksAndGrades;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Boolean getShowHeader() {
        return showHeader;
    }

    public void setShowHeader(Boolean showHeader) {
        this.showHeader = showHeader;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String[] getSubjectsArray() {
        return subjectsArray;
    }

    public void setSubjectsArray(String[] subjectsArray) {
        this.subjectsArray = subjectsArray;
    }

    public Map<String, String> getColorForGrades() {
        return colorForGrades;
    }

    public void setColorForGrades(Map<String, String> colorForGrades) {
        this.colorForGrades = colorForGrades;
    }

    public Map<String, String> getDefiningGrades() {
        return definingGrades;
    }

    public void setDefiningGrades(Map<String, String> definingGrades) {
        this.definingGrades = definingGrades;
    }

    public Map<String, Object> getMarksAndGrades() {
        return marksAndGrades;
    }

    public void setMarksAndGrades(Map<String, Object> marksAndGrades) {
        this.marksAndGrades = marksAndGrades;
    }

    @Override
    public String toString() {
        return "ReportCardVM{" +
            "studentName='" + studentName + '\'' +
            ", standard='" + standard + '\'' +
            ", admissionId='" + admissionId + '\'' +
            ", rollNo='" + rollNo + '\'' +
            ", attendance='" + attendance + '\'' +
            ", examName='" + examName + '\'' +
            ", schoolName='" + schoolName + '\'' +
            ", showHeader=" + showHeader +
            ", logoLink='" + logoLink + '\'' +
            ", remarks='" + remarks + '\'' +
            ", subjectsArray=" + Arrays.toString(subjectsArray) +
            ", colorForGrades=" + colorForGrades +
            ", definingGrades=" + definingGrades +
            ", marksAndGrades=" + marksAndGrades +
            '}';
    }
}
