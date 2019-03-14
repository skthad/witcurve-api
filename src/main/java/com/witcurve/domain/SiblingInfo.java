package com.witcurve.domain;

import com.witcurve.domain.enumeration.Gender;
import com.witcurve.service.util.LocalDateConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="sibling_info")
public class SiblingInfo extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "standard_during_admission", length = 50)
    private String standardDuringAdmission;

    @Column
    @Convert(converter = LocalDateConverter.class)
    private LocalDate studyingSince;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getStandardDuringAdmission() {
        return standardDuringAdmission;
    }

    public void setStandardDuringAdmission(String standardDuringAdmission) {
        this.standardDuringAdmission = standardDuringAdmission;
    }

    public LocalDate getStudyingSince() {
        return studyingSince;
    }

    public void setStudyingSince(LocalDate studyingSince) {
        this.studyingSince = studyingSince;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiblingInfo that = (SiblingInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SiblingInfo{" +
            "id=" + id +
            '}';
    }
}
