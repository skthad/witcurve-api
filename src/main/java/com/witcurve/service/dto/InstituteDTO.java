package com.witcurve.service.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class InstituteDTO extends AbstractAuditingDTO {

    private Long id;

    @NotNull
    private String name;

    public InstituteDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstituteDTO)) return false;
        InstituteDTO instituteDTO = (InstituteDTO) o;
        return Objects.equals(getId(), instituteDTO.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "InstituteDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
