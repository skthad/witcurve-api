package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportCardVM {

//    private ScholasticVM.ScholasticDetailsVM scholasticDetailVM = new ScholasticDetailsVM();
//    private  ScholasticVM scholasticVM = new ScholasticVM();
//    private  NonScholasticVM nonScholasticVM = new NonScholasticVM();
//    private ScholasticVM.ScholasticDetailsVM.OverallVM overallVM = new OverallVM();
//    private ScholasticVM.ScholasticDetailsVM.ExamDetailsVM.MarksAndGradeDetailsVM marksAndGradeDetailsVM = new MarksAndGradeDetailsVM();
//    private ScholasticVM.ScholasticDetailsVM.ExamDetailsVM examDetailsVM = new ScholasticVM.ScholasticDetailsVM.ExamDetailsVM();
//    private AttributeDetailVM attributeDetailVM = new AttributeDetailVM();
//    private AttributeVM attributeVM = new AttributeVM();

    @NotNull
    private String studentName;

    @NotNull
    private String standard;

    @NotNull
    private String admissionId;

    @NotNull
    private String attendance;

    @NotNull
    private Boolean showHeader = true;

    @NotNull
    private String logoLink;

    @NotNull
    private String schoolPrimaryColor = "#000000";

    private String remarks;

    private List<AttributeVM> attributes;

    private Map<String, String> colorForGrades;

    private Map<String, String> definingGrade;

    private Map<String, String> note;

    @NotNull
    private String title;

    private ScholasticVM scholastic;

    private NonScholasticVM nonScholastic;

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

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
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

    public String getSchoolPrimaryColor() {
        return schoolPrimaryColor;
    }

    public void setSchoolPrimaryColor(String schoolPrimaryColor) {
        this.schoolPrimaryColor = schoolPrimaryColor;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<AttributeVM> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeVM> attributes) {
        this.attributes = attributes;
    }

    public void addAttributes(AttributeVM attributeVM) {
        if(this.attributes == null) {
            this.attributes = new ArrayList<>();
        }
        this.attributes.add(attributeVM);
    }

    public Map<String, String> getColorForGrades() {
        return colorForGrades;
    }

    public void setColorForGrades(Map<String, String> colorForGrades) {
        this.colorForGrades = colorForGrades;
    }

    public Map<String, String> getDefiningGrade() {
        return definingGrade;
    }

    public void setDefiningGrade(Map<String, String> definingGrade) {
        this.definingGrade = definingGrade;
    }

    public Map<String, String> getNote() {
        return note;
    }

    public void setNote(Map<String, String> note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ScholasticVM getScholastic() {
        return scholastic;
    }

    public void setScholastic(ScholasticVM scholastic) {
        this.scholastic = scholastic;
    }

    public NonScholasticVM getNonScholastic() {
        return nonScholastic;
    }

    public void setNonScholastic(NonScholasticVM nonScholastic) {
        this.nonScholastic = nonScholastic;
    }

    public static class ScholasticVM {

        @NotNull
        private List<String> subjectsArray;

        @NotNull
        private ScholasticDetailsVM scholasticDetails;

        public List<String> getSubjectsArray() {
            return subjectsArray;
        }

        public void setSubjectsArray(List<String> subjectsArray) {
            this.subjectsArray = subjectsArray;
        }

        public ScholasticDetailsVM getScholasticDetails() {
            return scholasticDetails;
        }

        public void setScholasticDetails(ScholasticDetailsVM scholasticDetails) {
            this.scholasticDetails = scholasticDetails;
        }


        public static class ScholasticDetailsVM {

            private List<ExamDetailsVM> examDetails;

            private OverallVM overall;

            public List<ExamDetailsVM> getExamDetails() {
                return examDetails;
            }

            public void setExamDetails(List<ExamDetailsVM> examDetails) {
                this.examDetails = examDetails;
            }

            public void addExamDetails(ExamDetailsVM examDetailsVM) {
                if(this.examDetails == null) {
                    this.examDetails = new ArrayList<>();
                }
                this.examDetails.add(examDetailsVM);
            }

            public OverallVM getOverall() {
                return overall;
            }

            public void setOverall(OverallVM overall) {
                this.overall = overall;
            }

            @Override
            public String toString() {
                return "ScholasticDetailsVM{" +
                    "examDetails=" + examDetails +
                    ", overall=" + overall +
                    '}';
            }

            public static class ExamDetailsVM {

                @NotNull
                private String examName;

                @NotNull
                private List<MarksAndGradeDetailsVM> marksAndGradesDetails;

                public String getExamName() {
                    return examName;
                }

                public void setExamName(String examName) {
                    this.examName = examName;
                }

                public List<MarksAndGradeDetailsVM> getMarksAndGradesDetails() {
                    return marksAndGradesDetails;
                }

                public void setMarksAndGradesDetails(List<MarksAndGradeDetailsVM> marksAndGradesDetails) {
                    this.marksAndGradesDetails = marksAndGradesDetails;
                }

                public void addMarksAndGradeDetails(MarksAndGradeDetailsVM marksAndGradeDetailsVM) {
                    if(this.marksAndGradesDetails == null) {
                        this.marksAndGradesDetails = new ArrayList<>();
                    }
                    this.marksAndGradesDetails.add(marksAndGradeDetailsVM);
                }

                @Override
                public String toString() {
                    return "ExamDetailsVM{" +
                        "examName='" + examName + '\'' +
                        ", marksAndGradesDetails=" + marksAndGradesDetails +
                        '}';
                }

                public static class MarksAndGradeDetailsVM  {

                    @NotNull
                    private String name;

                    @NotNull
                    private String shortForm;

                    private Map<String, String> marks;

                    private Map<String, String> grade;

                    @NotNull
                    private Boolean showMarks = true;

                    @NotNull
                    private Boolean showGrade = true;

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
                    public Map<String, String> getMarks() {
                        return marks;
                    }

                    public void setMarks(Map<String, String> marks) {
                        this.marks = marks;
                    }

                    public Map<String, String> getGrade() {
                        return grade;
                    }

                    public void setGrade(Map<String, String> grade) {
                        this.grade = grade;
                    }

                    public Boolean getShowMarks() {
                        return showMarks;
                    }

                    public void setShowMarks(Boolean showMarks) {
                        this.showMarks = showMarks;
                    }

                    public Boolean getShowGrade() {
                        return showGrade;
                    }

                    public void setShowGrade(Boolean showGrade) {
                        this.showGrade = showGrade;
                    }

                    @Override
                    public String toString() {
                        return "MarksAndGradeDetailsVM{" +
                            "name='" + name + '\'' +
                            ", shortForm='" + shortForm + '\'' +
                            ", marks=" + marks +
                            ", grade=" + grade +
                            ", showMarks=" + showMarks +
                            ", showGrade=" + showGrade +
                            '}';
                    }
                }
            }

            public static class OverallVM {

                private Map<String, String> marks;

                private Map<String, String> grade;

                @NotNull
                private Boolean showMarks = true;

                @NotNull
                private Boolean showGrade = true;

                @NotNull
                private String overAllGrade;

                @NotNull
                private String overAllMarks;

                public Map<String, String> getMarks() {
                    return marks;
                }

                public void setMarks(Map<String, String> marks) {
                    this.marks = marks;
                }

                public Map<String, String> getGrade() {
                    return grade;
                }

                public void setGrade(Map<String, String> grade) {
                    this.grade = grade;
                }

                public Boolean getShowMarks() {
                    return showMarks;
                }

                public void setShowMarks(Boolean showMarks) {
                    this.showMarks = showMarks;
                }

                public Boolean getShowGrade() {
                    return showGrade;
                }

                public void setShowGrade(Boolean showGrade) {
                    this.showGrade = showGrade;
                }

                public String getOverAllGrade() { return overAllGrade; }

                public void setOverAllGrade(String overAllGrade) { this.overAllGrade = overAllGrade; }

                public String getOverAllMarks() { return overAllMarks; }

                public void setOverAllMarks(String overAllMarks) { this.overAllMarks = overAllMarks; }

                @Override
                public String toString() {
                    return "OverallVM{" +
                        "marks=" + marks +
                        ", grade=" + grade +
                        ", showMarks=" + showMarks +
                        ", showGrade=" + showGrade +
                        ", overAllGrade='" + overAllGrade + '\'' +
                        ", overAllMarks='" + overAllMarks + '\'' +
                        '}';
                }
            }
        }

        @Override
        public String toString() {
            return "ScholasticVM{" +
                "subjectsArray=" + subjectsArray +
                ", scholasticDetails=" + scholasticDetails +
                '}';
        }
    }

    public static class NonScholasticVM {

        @NotNull
        private String titleName = "Subjects";

        @NotNull
        private List<String> subjectsArray;

        @NotNull
        private List<AttributeDetailVM> nonScholasticDetails;

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public List<String> getSubjectsArray() {
            return subjectsArray;
        }

        public void setSubjectsArray(List<String> subjectsArray) {
            this.subjectsArray = subjectsArray;
        }
        public List<AttributeDetailVM> getNonScholasticDetails() {
            return nonScholasticDetails;
        }

        public void setNonScholasticDetails(List<AttributeDetailVM> nonScholasticDetails) {
            this.nonScholasticDetails = nonScholasticDetails;
        }

        public void addNonScholasticDetails(AttributeDetailVM attributeDetailVM) {
            if(this.nonScholasticDetails == null) {
                this.nonScholasticDetails = new ArrayList<>();
            }
            this.nonScholasticDetails.add(attributeDetailVM);
        }

        @Override
        public String toString() {
            return "NonScholasticVM{" +
                "titleName='" + titleName + '\'' +
                ", subjectsArray=" + subjectsArray +
                ", nonScholasticDetails=" + nonScholasticDetails +
                '}';
        }
    }

    public static class AttributeVM {

        @NotNull
        private String titleName;

        @NotNull
        private List<String> fieldArray;

        private List<String> boldColumnsArray;

        @NotNull
        private List<AttributeDetailVM> attributeDetails;

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public List<String> getFieldArray() {
            return fieldArray;
        }

        public void setFieldArray(List<String> fieldArray) {
            this.fieldArray = fieldArray;
        }

        public List<String> getBoldColumnsArray() {
            return boldColumnsArray;
        }

        public void setBoldColumnsArray(List<String> boldColumnsArray) {
            this.boldColumnsArray = boldColumnsArray;
        }

        public List<AttributeDetailVM> getAttributeDetails() {
            return attributeDetails;
        }

        public void addAttributeDetails(AttributeDetailVM attributeDetailVM) {
            if(this.attributeDetails == null) {
                this.attributeDetails = new ArrayList<>();
            }
            this.attributeDetails.add(attributeDetailVM);
        }

        public void setAttributeDetails(List<AttributeDetailVM> attributeDetails) {
            this.attributeDetails = attributeDetails;
        }

        @Override
        public String toString() {
            return "AttributeVM{" +
                "titleName='" + titleName + '\'' +
                ", fieldArray=" + fieldArray +
                ", boldColumnsArray=" + boldColumnsArray +
                ", attributeDetails=" + attributeDetails +
                '}';
        }
    }

    public static class AttributeDetailVM {

        @NotNull
        private String columnName;

        @NotNull
        private Map<String, String> values;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public Map<String, String> getValues() {
            return values;
        }

        public void setValues(Map<String, String> values) {
            this.values = values;
        }

        @Override
        public String toString() {
            return "AttributeDetailVM{" +
                "columnName='" + columnName + '\'' +
                ", values=" + values +
                '}';
        }
    }

    @Override
    public String toString() {
        return "ReportCardVM{" +
            "studentName='" + studentName + '\'' +
            ", standard='" + standard + '\'' +
            ", admissionId='" + admissionId + '\'' +
            ", attendance='" + attendance + '\'' +
            ", showHeader=" + showHeader +
            ", logoLink='" + logoLink + '\'' +
            ", schoolPrimaryColor='" + schoolPrimaryColor + '\'' +
            ", remarks='" + remarks + '\'' +
            ", attributes=" + attributes +
            ", colorForGrades=" + colorForGrades +
            ", definingGrade=" + definingGrade +
            ", note=" + note +
            ", title='" + title + '\'' +
            ", scholastic=" + scholastic +
            ", nonScholastic=" + nonScholastic +
            '}';
    }
}
