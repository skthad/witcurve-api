package com.witcurve.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="course", uniqueConstraints = {
    @UniqueConstraint( name= "course_name_school_id_UK",
        columnNames = {"course_name", "school_id"})
})
public class Course extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courseIdSeq")
    @SequenceGenerator(name = "courseIdSeq", sequenceName="course_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 50, name = "course_name")
    private String courseName;

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
    private School school;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
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
            ", courseName='" + courseName + '\'' +
            ", description='" + description + '\'' +
            ", eligibleForSubstitute=" + eligibleForSubstitute +
            ", masterSubject=" + masterSubject +
            ", school=" + school +
            '}';
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
