package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.Grade;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PeriodicTestDTO extends AbstractAuditingDTO {

    private String bindingId;

    private String name;

    private String description;

    private List<LocalDate> dateList;

    private List<Grade> grades;

    public String getBindingId() {
        return bindingId;
    }

    public void setBindingId(String bindingId) {
        this.bindingId = bindingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LocalDate> getDateList() {
        return dateList;
    }

    public void setDateList(List<LocalDate> dateList) {
        this.dateList = dateList;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodicTestDTO that = (PeriodicTestDTO) o;
        return bindingId.equals(that.bindingId) &&
            name.equals(that.name) &&
            description.equals(that.description) &&
            dateList.equals(that.dateList) &&
            grades.equals(that.grades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindingId, name, description, dateList, grades);
    }

    @Override
    public String toString() {
        return "PeriodicTestDTO{" +
            "bindingId='" + bindingId + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", dateList=" + dateList +
            ", grades=" + grades +
            '}';
    }

}
