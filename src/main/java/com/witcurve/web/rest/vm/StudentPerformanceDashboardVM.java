package com.witcurve.web.rest.vm;

import com.witcurve.domain.enumeration.Gender;

import java.util.Objects;

public class StudentPerformanceDashboardVM {

    private long standardId;

    private Gender gender;

    private long count;

    public StudentPerformanceDashboardVM() {

    }

    public StudentPerformanceDashboardVM(long standardId, Gender gender, long count) {
        this.standardId = standardId;
        this.gender = gender;
        this.count = count;
    }

    public long getStandardId() {
        return standardId;
    }

    public void setStandardId(long standardId) {
        this.standardId = standardId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentPerformanceDashboardVM that = (StudentPerformanceDashboardVM) o;
        return Objects.equals(standardId, that.standardId) &&
            gender == that.gender &&
            Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(standardId, gender, count);
    }
}
