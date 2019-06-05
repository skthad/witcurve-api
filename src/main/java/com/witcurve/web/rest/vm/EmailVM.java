package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * View Model object for storing Email request
 */
public class EmailVM {

    private String studentList;

    private String staffList;

    private String standardList;

    private String gradeList;

    @NotNull
    private String subject;

    @NotNull
    private String body;

    public String getStudentList() {
        return studentList;
    }

    public void setStudentList(String studentList) {
        this.studentList = studentList;
    }

    public String getStaffList() {
        return staffList;
    }

    public void setStaffList(String staffList) {
        this.staffList = staffList;
    }

    public String getStandardList() {
        return standardList;
    }

    public void setStandardList(String standardList) {
        this.standardList = standardList;
    }

    public String getGradeList() {
        return gradeList;
    }

    public void setGradeList(String gradeList) {
        this.gradeList = gradeList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailVM emailVM = (EmailVM) o;
        return Objects.equals(studentList, emailVM.studentList) &&
            Objects.equals(staffList, emailVM.staffList) &&
            Objects.equals(standardList, emailVM.standardList) &&
            Objects.equals(gradeList, emailVM.gradeList) &&
            Objects.equals(subject, emailVM.subject) &&
            Objects.equals(body, emailVM.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentList, staffList, standardList, gradeList, subject, body);
    }

    @Override
    public String toString() {
        return "EmailVM{" +
            "studentList='" + studentList + '\'' +
            ", staffList='" + staffList + '\'' +
            ", standardList='" + standardList + '\'' +
            ", gradeList='" + gradeList + '\'' +
            ", subject='" + subject + '\'' +
            ", body='" + body + '\'' +
            '}';
    }
}
