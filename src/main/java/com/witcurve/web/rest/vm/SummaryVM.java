package com.witcurve.web.rest.vm;

import java.util.Map;
import java.util.Objects;

public class SummaryVM {

    private Map<String, Long> mapOfAnswerAndCount;

    private Long count;

    public Map<String, Long> getMapOfAnswerAndCount() {
        return mapOfAnswerAndCount;
    }

    public void setMapOfAnswerAndCount(Map<String, Long> mapOfAnswerAndCount) {
        this.mapOfAnswerAndCount = mapOfAnswerAndCount;
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
        SummaryVM summaryVM = (SummaryVM) o;
        return Objects.equals(mapOfAnswerAndCount, summaryVM.mapOfAnswerAndCount) &&
            Objects.equals(count, summaryVM.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapOfAnswerAndCount, count);
    }

    @Override
    public String toString() {
        return "SummaryVM{" +
            "mapOfAnswerAndCount=" + mapOfAnswerAndCount +
            ", count=" + count +
            '}';
    }
}
