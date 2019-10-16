package com.witcurve.service.dto;

import java.util.List;

public class StudentPerformanceDTO {

    List<ExamMarksDTO> exams;

    public static class ExamMarksDTO {

        String examName;
        List<CourseMarksDTO> courseMarks;

        public static class CourseMarksDTO {
            String subjectName;
            Double marks;
            Double sectionAvg;
            Double classAvg;

            public String getSubjectName() {
                return subjectName;
            }

            public void setSubjectName(String subjectName) {
                this.subjectName = subjectName;
            }

            public Double getMarks() {
                return marks;
            }

            public void setMarks(Double marks) {
                this.marks = marks;
            }

            public Double getSectionAvg() {
                return sectionAvg;
            }

            public void setSectionAvg(Double sectionAvg) {
                this.sectionAvg = sectionAvg;
            }

            public Double getClassAvg() {
                return classAvg;
            }

            public void setClassAvg(Double classAvg) {
                this.classAvg = classAvg;
            }

            @Override
            public String toString() {
                return "CourseMarksDTO{" +
                    "subjectName='" + subjectName + '\'' +
                    ", marks=" + marks +
                    ", sectionAvg=" + sectionAvg +
                    ", classAvg=" + classAvg +
                    '}';
            }
        }

        public String getExamName() {
            return examName;
        }

        public void setExamName(String examName) {
            this.examName = examName;
        }

        public List<CourseMarksDTO> getCourseMarks() {
            return courseMarks;
        }

        public void setCourseMarks(List<CourseMarksDTO> courseMarks) {
            this.courseMarks = courseMarks;
        }

        @Override
        public String toString() {
            return "ExamMarksDTO{" +
                "examName='" + examName + '\'' +
                ", courseMarks=" + courseMarks +
                '}';
        }
    }

    public List<ExamMarksDTO> getExams() {
        return exams;
    }

    public void setExams(List<ExamMarksDTO> exams) {
        this.exams = exams;
    }

    @Override
    public String toString() {
        return "StudentPerformanceDTO{" +
            "exams=" + exams +
            '}';
    }
}
