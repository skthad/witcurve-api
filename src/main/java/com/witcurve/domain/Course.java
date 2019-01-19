package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="course", uniqueConstraints = {
    @UniqueConstraint( name= "course_code_school_info_id_UK",
        columnNames = {"course_code", "school_info_id"})
})
public class Course extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseIdSeq")
    @SequenceGenerator(name = "courseIdSeq", sequenceName="course_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 50, name = "course_code")
    private String courseCode;

    @Column
    private String description;

    @Column
    private Boolean eligibleForSubstitute;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private MasterSubject masterSubject;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolInfo schoolInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getEligibleForSubstitute() {
        return eligibleForSubstitute;
    }

    public void setEligibleForSubstitute(Boolean eligibleForSubstitute) {
        this.eligibleForSubstitute = eligibleForSubstitute;
    }

    public MasterSubject getMasterSubject() {
        return masterSubject;
    }

    public void setMasterSubject(MasterSubject masterSubject) {
        this.masterSubject = masterSubject;
    }

    public SchoolInfo getSchoolInfo() {
        return schoolInfo;
    }

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
    }

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
            ", courseCode='" + courseCode + '\'' +
            ", description='" + description + '\'' +
            ", eligibleForSubstitute=" + eligibleForSubstitute +
            ", masterSubject=" + masterSubject +
            ", schoolInfo=" + schoolInfo +
            '}';
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
