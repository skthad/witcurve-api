package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;

/**
 * View Model object for storing SMS request
 */
public class SmsVM {

    private String studentList;

    private String staffList;

    private String standardList;

    private String gradeList;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
