package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.FeeDetailsType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class FeeDetailsDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Long schoolInfoId;

    @NotNull
    private String name;

    @NotNull
    private FeeDetailsType type;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getSchoolInfoId() { return schoolInfoId; }

    public void setSchoolInfoId(Long schoolInfoId) { this.schoolInfoId = schoolInfoId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public FeeDetailsType getType() { return type; }

    public void setType(FeeDetailsType type) { this.type = type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeeDetailsDTO that = (FeeDetailsDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FeeDetailsDTO{" +
            "id=" + id +
            '}';
    }
}
