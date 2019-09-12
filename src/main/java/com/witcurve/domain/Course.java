package com.witcurve.domain;

import com.witcurve.domain.enumeration.CourseType;
import com.witcurve.domain.enumeration.Grade;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="course")
public class Course extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolInfo schoolInfo;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private MasterSubject masterSubject;

    @NotNull
    @Column(nullable = false, length = 50, name = "course_code")
    private String courseCode;

    @Column
    private String description;

    @NotNull
    @Column(length=50, name = "course_type", nullable = false, columnDefinition = "varchar(50) default 'SCHOLASTIC'")
    @Enumerated(EnumType.STRING)
    private CourseType courseType = CourseType.SCHOLASTIC;

    @Column
    private Boolean contentPublished;

    @NotNull
    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    @NotNull
    @Column(name= "elective", nullable = false, columnDefinition = "boolean default false")
    private Boolean elective = false;

    @NotNull
    @Column(name="mandatory", nullable = false, columnDefinition = "boolean default true")
    private Boolean mandatory = true;

    @NotNull
    @Column(nullable = false,name="display_name")
    private String displayName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public MasterSubject getMasterSubject() {
        return masterSubject;
    }

    public void setMasterSubject(MasterSubject masterSubject) {
        this.masterSubject = masterSubject;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getContentPublished() {
        return contentPublished;
    }

    public void setContentPublished(Boolean contentPublished) {
        this.contentPublished = contentPublished;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getElective() {
        return elective;
    }

    public void setElective(Boolean elective) {
        this.elective = elective;
    }

    public Boolean getMandatory() { return mandatory; }

    public void setMandatory(Boolean mandatory) { this.mandatory = mandatory; }

    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public CourseType getCourseType() { return courseType; }

    public void setCourseType(CourseType courseType) { this.courseType = courseType; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + id +
            ", schoolInfo=" + schoolInfo +
            ", grade=" + grade +
            ", masterSubject=" + masterSubject +
            ", courseCode='" + courseCode + '\'' +
            ", description='" + description + '\'' +
            ", courseType=" + courseType +
            ", elective=" + elective +
            ", mandatory=" + mandatory +
            ", contentPublished=" + contentPublished +
            ", active=" + active +
            '}';
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
