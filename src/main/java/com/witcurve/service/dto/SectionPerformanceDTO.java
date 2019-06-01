package com.witcurve.service.dto;

import java.util.ArrayList;
import java.util.List;

public class SectionPerformanceDTO {

    String sectionName;

    Double averageScore;

    Integer studentCount;

    List<SubjectPerformanceDTO> subjectPerformances = new ArrayList<>();

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public List<SubjectPerformanceDTO> getSubjectPerformances() {
        return subjectPerformances;
    }

    public void setSubjectPerformances(List<SubjectPerformanceDTO> subjectPerformances) {
        this.subjectPerformances = subjectPerformances;
    }
}
