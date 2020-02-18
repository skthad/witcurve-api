package com.witcurve.web.rest.vm;

import java.util.Objects;

public class StandardStudentCountVM {

    private Long standardId;

    private long count;

    public StandardStudentCountVM(Long standardId, long count) {
        this.standardId = standardId;
        this.count = count;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
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
        StandardStudentCountVM that = (StandardStudentCountVM) o;
        return count == that.count &&
            Objects.equals(standardId, that.standardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(standardId, count);
    }

    @Override
    public String toString() {
        return "StandardStudentCountVM{" +
            "standardId=" + standardId +
            ", count=" + count +
            '}';
    }
}
