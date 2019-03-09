package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class StaffEligibilityDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private StaffDTO staff;

    @NotNull
    private Grade grade;

    @NotNull
    private String masterSubject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StaffDTO getStaff() {
        return staff;
    }

    public void setStaff(StaffDTO staff) {
        this.staff = staff;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public String getMasterSubject() {
        return masterSubject;
    }

    public void setMasterSubject(String masterSubject) {
        this.masterSubject = masterSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StaffEligibilityDTO)) return false;
        StaffEligibilityDTO that = (StaffEligibilityDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "StaffEligibilityDTO{" +
            "id=" + id +
            ", staffId='" + staff.getId()+ '\'' +
            ", grade='" + grade+ '\'' +
            ", masterSubject='" + masterSubject+ '\'' +
            '}';
    }
}
