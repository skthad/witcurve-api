package com.witcurve.web.rest.vm;

import com.witcurve.domain.enumeration.StaffType;

import java.util.Objects;

public class StaffVM {

    private StaffType type;

    private Long count;

    public StaffVM(StaffType type, Long count) {
        this.type = type;
        this.count = count;
    }

    public StaffType getType() {
        return type;
    }

    public void setType(StaffType type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffVM staffVM = (StaffVM) o;
        return type == staffVM.type &&
            Objects.equals(count, staffVM.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, count);
    }
}
