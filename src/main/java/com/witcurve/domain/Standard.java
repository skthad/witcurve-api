package com.witcurve.domain;

import com.witcurve.domain.enumeration.Grade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="standard", uniqueConstraints = {
    @UniqueConstraint(name = "grade_section_schoolInfo_UK",
        columnNames = {"grade", "section", "school_info_id"})
})
public class Standard extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "standardIdSeq", sequenceName="standard_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private SchoolInfo schoolInfo;

    @NotNull
    @Column(nullable = false, length = 50)
    private Grade grade;

    @Column(length = 50)
    private String section;

    @OneToOne
    @JoinColumn(unique = true)
    private Staff classTeacher;

    @NotNull
    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Staff getClassTeacher() {
        return classTeacher;
    }

    public void setClassTeacher(Staff classTeacher) {
        this.classTeacher = classTeacher;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Standard)) return false;
        Standard standard = (Standard) o;
        return Objects.equals(getId(), standard.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Standard{" +
            "id=" + id +
            '}';
    }
}
